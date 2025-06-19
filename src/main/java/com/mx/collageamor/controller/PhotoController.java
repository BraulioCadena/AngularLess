package com.mx.collageamor.controller;

import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "https://collagelessx.netlify.app") // Cambia esto a tu dominio real al final
@RestController
@RequestMapping("/api/photos")
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/debug/ping")
    public String ping() {
        return "🟢 Backend Cloudinary listo";
    }
}
