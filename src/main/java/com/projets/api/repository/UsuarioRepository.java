package com.projets.api.repository;

import com.projets.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importamos List
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // --- Métodos existentes ---
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);

    // --- NUEVOS MÉTODOS PARA EL ADMIN DE USUARIOS ---

    /**
     * Busca usuarios cuyo nombre, email o teléfono contenga el término de búsqueda.
     * Ignora mayúsculas/minúsculas.
     */
    List<Usuario> findByNombreCompletoContainingIgnoreCaseOrEmailContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
            String nombre, String email, String telefono);

    /**
     * Busca un usuario por email, pero que NO tenga un ID específico.
     * Lo usaremos para validar la edición:
     * "Quiero saber si 'test@test.com' ya existe PARA OTRO usuario que no sea el que estoy editando"
     */
    Optional<Usuario> findByEmailAndIdNot(String email, Integer id);
    
    List<Usuario> findAllByOrderByIdAsc();
    
}