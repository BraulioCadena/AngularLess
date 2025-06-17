package com.mx.collageamor.service.impl;

import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.repository.PhotoRepository;
import com.mx.collageamor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository repository;
    private final String uploadDir = "/mnt/data/uploads"; // ruta persistente en Render
    private final String backendUrl = "https://collageamor-backend.onrender.com"; // cámbiala si usas otro dominio

    @Override
    public Photo save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(fileName);
            Files.createDirectories(path.getParent());

            System.out.println("📥 Guardando archivo en: " + path.toAbsolutePath());

            long copiedBytes = Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            if (copiedBytes == 0) throw new IOException("Archivo vacío. No se copió nada.");

            String fullUrl = backendUrl + "/api/photos/download/" + fileName;
            System.out.println("📸 Imagen disponible en: " + fullUrl);

            Photo photo = new Photo();
            photo.setFilename(fileName);
            photo.setUrl(fullUrl);

            return repository.save(photo);

        } catch (IOException e) {
            System.err.println("❌ Error al guardar imagen: " + e.getMessage());
            throw new RuntimeException("Error al guardar imagen", e);
        }
    }

    @Override
    public List<Photo> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Photo> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(photo -> {
            Path path = Paths.get(uploadDir, photo.getFilename());
            try {
                Files.deleteIfExists(path);
                System.out.println("🗑️ Archivo eliminado: " + path.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("⚠️ No se pudo eliminar archivo: " + e.getMessage());
            }
            repository.delete(photo);
        });
    }
}
