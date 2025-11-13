package com.projets.api.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
    private String codigoEmpresa; 
    
}