package com.projets.api.repository;

import com.projets.api.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    /**
     * Busca las 5 tareas de un usuario que NO estén en el estado proporcionado,
     * ordenadas por la fecha de vencimiento más próxima.
     * * Traducción SQL:
     * SELECT * FROM Tarea 
     * WHERE id_usuario_asignado = ? 
     * AND estado != ?
     * ORDER BY fecha_vencimiento ASC
     * LIMIT 5
     */
    List<Tarea> findTop5ByUsuarioAsignadoIdAndEstadoNotOrderByFechaVencimientoAsc(Integer idUsuario, String estado);

}