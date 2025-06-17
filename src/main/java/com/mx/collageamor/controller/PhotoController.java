package com.mx.collageamor.controller;

import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@CrossOrigin(origins = "https://collagelessx.netlify.app")
@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService service;
    private final String uploadDir = "/mnt/data/uploads";

    @PostMapping("/upload")
    public Photo upload(@RequestParam("file") MultipartFile file) {
        return service.save(file);
    }

    @GetMapping
    public List<Photo> getAll() {
        return service.getAll();
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        try {
            Path path = Paths.get(uploadDir, filename);
            if (!Files.exists(path)) {
                System.out.println("🚫 Archivo no encontrado en: " + path.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(path.toUri());
            String contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            System.err.println("❌ Error al servir imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 🔍 Ruta auxiliar para ver qué archivos hay en /mnt/data/uploads
    @GetMapping("/debug/files")
    public List<String> listFiles() {
        File folder = new File(uploadDir);
        String[] files = folder.list();
        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }
}
