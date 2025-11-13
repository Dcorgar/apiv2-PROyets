package com.projets.api.controller;

import com.projets.api.dto.TareaDashboardDTO;
import com.projets.api.model.Tarea;
import com.projets.api.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tareas") // URL base para Tareas
@CrossOrigin(origins = "*") // Permitimos CORS
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    /**
     * Endpoint para obtener las 5 próximas tareas del dashboard de un usuario.
     */
    @GetMapping("/dashboard/usuario/{idUsuario}")
    public ResponseEntity<List<TareaDashboardDTO>> getTareasDashboard(
            @PathVariable Integer idUsuario) {

        // 1. Definimos el estado que queremos excluir (ej. "completada")
        final String ESTADO_EXCLUIDO = "completada";

        // 2. Llamamos al método mágico del repositorio
        List<Tarea> tareas = tareaRepository
                .findTop5ByUsuarioAsignadoIdAndEstadoNotOrderByFechaVencimientoAsc(
                        idUsuario, ESTADO_EXCLUIDO);

        // 3. Convertimos la lista de Entidades (Tarea) a una lista de DTOs
        List<TareaDashboardDTO> dtos = tareas.stream()
                .map(TareaDashboardDTO::new) // Llama al constructor que creamos
                .collect(Collectors.toList());

        // 4. Devolvemos la lista de DTOs (solo los 3 campos)
        return ResponseEntity.ok(dtos);
    }

    // Aquí irán futuros métodos GET, POST, PUT, DELETE para Tareas
}