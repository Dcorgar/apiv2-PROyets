package com.projets.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// El JSON que responderemos si el login es exitoso
@Data
@AllArgsConstructor // Genera el constructor que usaremos
public class LoginResponse {
    private String message;
    private String token;
}