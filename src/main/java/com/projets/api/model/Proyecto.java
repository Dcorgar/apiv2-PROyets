package com.projets.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date; // Usamos java.util.Date para las fechas de SQL

@Data
@Entity
@Table(name = "proyecto")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // No necesitamos mapear id_empresa e id_responsable como objetos
    // por ahora, los mantenemos simples como IDs.
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "id_responsable")
    private Integer idResponsable;

    @Column(name = "codigo_proyecto")
    private String codigoProyecto;

    private String nombre;
    private String descripcion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_fin_estimada")
    private Date fechaFinEstimada;

    private String estado;
    private Integer prioridad;
}