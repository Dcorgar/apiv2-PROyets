package com.projets.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación: Muchas Tareas pertenecen a Un Proyecto
    @ManyToOne
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id")
    private Proyecto proyecto;

    // Relación: Muchas Tareas son asignadas a Un Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario_asignado", referencedColumnName = "id")
    private Usuario usuarioAsignado;

    private String nombre;
    private String descripcion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    private String estado;
    private Integer prioridad;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", updatable = false) // updatable=false para que no se cambie
    private Date fechaCreacion;

    // JPA se encarga del id_proyecto e id_usuario_asignado
    // a través de los objetos Proyecto y Usuario.
}