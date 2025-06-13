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
import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService service;

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
            Path path = Paths.get("uploads", filename);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) throw new FileNotFoundException("Archivo no encontrado");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    
}