package com.projets.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(unique = true)
    private String codigo;

    // Nota: No necesitamos la lista de usuarios aquí de momento,
    // lo mantenemos simple para el registro.
}