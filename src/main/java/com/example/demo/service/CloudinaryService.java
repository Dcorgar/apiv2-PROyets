package com.example.demo.service;

import com.example.demo.dto.SugerenciaResponse;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Pequeña utilidad para poner la primera letra en mayúscula
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public SugerenciaResponse uploadAndAnalyze(MultipartFile file) throws IOException {

        // 1. Subir la imagen (modo "Unsigned" como en Postman)
        Map<String, Object> uploadResult = cloudinary.uploader().unsignedUpload(
            file.getBytes(),    // El archivo
            "prueba_v2",        // Tu preset (que funciona)
            Map.of()            // Opciones extra (ninguna)
        );

        // 2. Preparar el objeto de respuesta
        SugerenciaResponse sugerencias = new SugerenciaResponse();


        // --- 3. NUEVA FORMA: PARSEO DE STRING "BRUTO" (Tu idea) ---
        
        // Convertimos el Map entero a un String, igual que en la "RESPUESTA CRUDA"
        String rawResponse = uploadResult.toString();

        try {
            String anchor = "caption="; // La "ancla" que queremos buscar
            int startIndex = rawResponse.indexOf(anchor);

            if (startIndex != -1) {
                // Encontramos "caption="
                // Movemos el índice al INICIO del texto de la descripción
                startIndex = startIndex + anchor.length(); 
                
                // Buscamos el FINAL de la descripción (el primer '}' que encuentre)
                int endIndex = rawResponse.indexOf("}", startIndex); 

                if (endIndex != -1) {
                    // ¡Cortamos el trozo!
                    String descripcion = rawResponse.substring(startIndex, endIndex);
                    System.out.println("DEBUG (String Parse): ¡Descripción encontrada! -> " + descripcion);
                    sugerencias.setDescripcionSugerida(descripcion);
                }
            } else {
                // Si ni siquiera el String "caption=" existe
                System.err.println("WARN (String Parse): La clave 'caption=' no se encontró en la respuesta cruda.");
            }
        } catch (Exception e) {
            System.err.println("Error grave al parsear la RESPUESTA CRUDA como String: " + e.getMessage());
        }
        

        // --- 4. FORMA ANTIGUA: PARSEO DE MAP (Solo para Título y Categoría) ---
        // Mantenemos la lógica de "coco" (que no fallaba)
        try {
            if (uploadResult.containsKey("info")) {
                Map<String, Object> info = (Map<String, Object>) uploadResult.get("info");
                if (info != null && info.containsKey("detection")) {
                    Map<String, Object> detection = (Map<String, Object>) info.get("detection");
                    Map<String, Object> objectDetection = (Map<String, Object>) detection.get("object_detection");
                    Map<String, Object> data = (Map<String, Object>) objectDetection.get("data");
                    Map<String, Object> coco = (Map<String, Object>) data.get("coco");
                    Map<String, Object> tagsMap = (Map<String, Object>) coco.get("tags");
                    String primerTag = tagsMap.keySet().stream().findFirst().orElse(null);
                    
                    if (primerTag != null) {
                        sugerencias.setTituloSugerido(capitalize(primerTag));
                        List<Map<String, Object>> tagData = (List<Map<String, Object>>) tagsMap.get(primerTag);
                        List<String> categories = (List<String>) tagData.get(0).get("categories");
                        if (categories != null && !categories.isEmpty()) {
                            sugerencias.setCategoriaSugerida(capitalize(categories.get(0)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error grave al parsear datos de COCO: " + e.getMessage());
        }

        // 5. Devolver el objeto
        return sugerencias;
    }
}