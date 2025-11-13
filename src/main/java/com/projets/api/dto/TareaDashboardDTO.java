package com.projets.api.dto;

import com.projets.api.model.Tarea; // Importamos la entidad
import lombok.Data;

import java.util.Date;

@Data
public class TareaDashboardDTO {

    private String nombre;
    private Date fechaVencimiento;
    private Integer prioridad;

    // Constructor para transformar la Entidad (Tarea) en este DTO
    public TareaDashboardDTO(Tarea tarea) {
        this.nombre = tarea.getNombre();
        this.fechaVencimiento = tarea.getFechaVencimiento();
        this.prioridad = tarea.getPrioridad();
    }
}