package com.projets.api.dto;

import com.projets.api.model.Usuario;
import lombok.Data;

@Data
public class UsuarioAdminDTO {

    private Integer id;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String password = "**********"; // Siempre enmascarada

    // Constructor para transformar la Entidad (Usuario) en este DTO
    public UsuarioAdminDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
    }
}