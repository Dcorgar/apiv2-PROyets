package com.projets.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Mantenemos este
import org.springframework.web.bind.annotation.*;

import com.projets.api.dto.LoginRequest;
import com.projets.api.dto.LoginResponse;
import com.projets.api.dto.RegistroRequest;
import com.projets.api.model.Empresa;
import com.projets.api.model.Usuario;
import com.projets.api.repository.EmpresaRepository;
import com.projets.api.repository.UsuarioRepository;
import com.projets.api.service.JwtService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Necesario para la comprobación manual

    @Autowired
    private JwtService jwtService; // Necesario para generar el token

    // NOTA: 'AuthenticationManager' ya no se inyecta,
    // porque haremos la validación manualmente.

    //
    // --- MÉTODO DE LOGIN (COMPLETAMENTE REESCRITO) ---
    //
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        // 1. Buscar al usuario por email
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(loginRequest.getEmail());
        
        // 2. Buscar la empresa por código
        Optional<Empresa> empresaOptional = empresaRepository.findByCodigo(loginRequest.getCodigoEmpresa());

        // 3. Validar que ambos existan
        if (usuarioOptional.isEmpty() || empresaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOptional.get();
        Empresa empresa = empresaOptional.get();

        // 4. Validar la contraseña
        // Comparamos el texto plano del request con el hash de la BDD
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // 5. ¡LA NUEVA VALIDACIÓN!
        // Validar que el usuario pertenezca a la empresa
        if (!usuario.getIdEmpresa().equals(empresa.getId())) {
            // El usuario existe, la contraseña es correcta, pero está intentando
            // entrar por la "puerta" de otra empresa.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // 6. Si las 3 comprobaciones (usuario, pass, empresa) son correctas:
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponse("Login exitoso", token));
    }


    //
    // --- MÉTODO DE REGISTRO (SIN CAMBIOS) ---
    //
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest registroRequest) {

        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("El email ya está en uso.");
        }

        Optional<Empresa> empresaOptional = empresaRepository.findByCodigo(registroRequest.getCodigoEmpresa());
        
        if (empresaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("El código de empresa no existe.");
        }

        Empresa empresa = empresaOptional.get();
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(registroRequest.getEmail());
        nuevoUsuario.setNombreCompleto(registroRequest.getNombreCompleto());
        nuevoUsuario.setTelefono(registroRequest.getTelefono());
        
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(registroRequest.getPassword()));
        nuevoUsuario.setIdEmpresa(empresa.getId());

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("Usuario registrado exitosamente.");
    }
}