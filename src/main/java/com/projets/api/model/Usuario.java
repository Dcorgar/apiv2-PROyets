package com.projets.api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importamos UserDetails

import java.util.Collection;
import java.util.List; // Importamos List

@Data
@Entity
@Table(name = "usuario")
// Hacemos que Usuario "sea" un UserDetails
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    private String telefono;

    // --- MÉTODOS REQUERIDOS POR UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // De momento no usamos roles (ej. "ROLE_ADMIN"), así que devolvemos una lista vacía.
        return List.of(); 
    }

    @Override
    public String getPassword() {
        return this.passwordHash; // Spring usará esta columna para la contraseña
    }

    @Override
    public String getUsername() {
        return this.email; // Spring usará el email como "nombre de usuario"
    }

    // Dejamos estos como 'true' por defecto
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}