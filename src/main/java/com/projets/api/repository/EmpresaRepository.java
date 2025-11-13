package com.projets.api.repository;

import com.projets.api.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    // Spring Boot creará la consulta:
    // "SELECT * FROM empresa WHERE codigo = ?"
    Optional<Empresa> findByCodigo(String codigo);
    
}