package com.projets.api.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    // Solo los 3 campos que se pueden editar
    private String nombreCompleto;
    private String email;
    private String telefono;
}