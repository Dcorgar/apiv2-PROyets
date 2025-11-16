package com.example.demo.controller;

import com.example.demo.dto.SugerenciaResponse;
import com.example.demo.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite llamadas desde tu index.html
public class UploadController {

    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<SugerenciaResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Llama al servicio para hacer el trabajo
            SugerenciaResponse sugerencias = cloudinaryService.uploadAndAnalyze(file);
            return ResponseEntity.ok(sugerencias);
            
        } catch (Exception e) {
            // Si algo falla, imprime el error en la consola de Spring
            e.printStackTrace();
            // Y devuelve un error 500 al frontend
            return ResponseEntity.status(500).body(null);
        }
    }
}