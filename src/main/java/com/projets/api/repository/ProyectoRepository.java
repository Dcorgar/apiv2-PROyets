package com.projets.api.repository;

import com.projets.api.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    // De momento no necesitamos métodos personalizados aquí
}