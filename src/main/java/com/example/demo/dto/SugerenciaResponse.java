package com.example.demo.dto;

import lombok.Data;

// @Data (de Lombok) crea getters, setters, etc. automáticamente
@Data
public class SugerenciaResponse {
    
    private String tituloSugerido;
    private String categoriaSugerida;
    private String descripcionSugerida;

}