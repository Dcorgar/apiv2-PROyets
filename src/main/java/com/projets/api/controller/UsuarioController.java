package com.projets.api.controller;

import com.projets.api.dto.UsuarioAdminDTO;
import com.projets.api.dto.UsuarioUpdateDTO;
import com.projets.api.model.Usuario;
import com.projets.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") 
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para OBTENER/BUSCAR usuarios. (Sin cambios)
     */
    @GetMapping
    public ResponseEntity<List<UsuarioAdminDTO>> buscarUsuarios(
            @RequestParam(required = false) String search) {
        
        List<Usuario> usuarios;

        if (search == null || search.trim().isEmpty()) {
        	usuarios = usuarioRepository.findAllByOrderByIdAsc();
        } else {
            usuarios = usuarioRepository
                .findByNombreCompletoContainingIgnoreCaseOrEmailContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
                    search, search, search);
        }

        List<UsuarioAdminDTO> dtos = usuarios.stream()
                .map(UsuarioAdminDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para EDITAR (Actualizar) un usuario. (Sin cambios)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Integer id, 
            @RequestBody UsuarioUpdateDTO updateDTO) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Usuario no encontrado.");
        }

        Optional<Usuario> emailEnUso = usuarioRepository
            .findByEmailAndIdNot(updateDTO.getEmail(), id);
        
        if (emailEnUso.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("El email ya está en uso por otro usuario.");
        }

        Usuario usuarioAActualizar = usuarioOptional.get();
        usuarioAActualizar.setNombreCompleto(updateDTO.getNombreCompleto());
        usuarioAActualizar.setEmail(updateDTO.getEmail());
        usuarioAActualizar.setTelefono(updateDTO.getTelefono());
        
        usuarioRepository.save(usuarioAActualizar);

        return ResponseEntity.ok().build();
    }

    //
    // --- ¡¡NUEVO MÉTODO AÑADIDO!! ---
    //
    /**
     * Endpoint para ELIMINAR un usuario.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {

        // 1. Verificar que el usuario existe
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Usuario no encontrado.");
        }

        // 2. Intentar eliminar
        try {
            // Esto fallará si el usuario es 'fk_responsable' en un 'Proyecto'
            // gracias al 'ON DELETE RESTRICT' de tu BDD.
            usuarioRepository.deleteById(id);
            
        } catch (Exception e) {
            // Capturamos el error de la BDD (DataIntegrityViolationException)
            // y devolvemos un error amigable.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar. El usuario es responsable de uno o más proyectos.");
        }

        // 3. Devolver 204 No Content (éxito, sin cuerpo)
        return ResponseEntity.noContent().build();
    }
}