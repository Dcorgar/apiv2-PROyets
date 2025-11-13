package com.projets.api.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    
    // Los 4 campos que mencionaste
    private String nombreCompleto;
    private String email;
    private String password;
    private String telefono;

    // El campo que necesitamos para cumplir con el 'NOT NULL' de la BD
    private String codigoEmpresa; 
}