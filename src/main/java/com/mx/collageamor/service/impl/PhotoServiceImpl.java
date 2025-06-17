package com.mx.collageamor.service.impl;

import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.repository.PhotoRepository;
import com.mx.collageamor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository repository;
    private final String uploadDir = "/mnt/data/uploads";
    private final String backendUrl = "https://collageamor-backend.onrender.com";

    @Override
    public Photo save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(fileName);
            Files.createDirectories(path.getParent());

            long copiedBytes = Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            if (copiedBytes == 0) throw new IOException("Archivo vacío, no se copió nada.");

            // 🟩 Aquí colocas esa línea
            String fullUrl = "https://collageamor-backend.onrender.com/api/photos/download/" + fileName;

            Photo photo = new Photo();
            photo.setFilename(fileName);
            photo.setUrl(fullUrl); // <-- AQUÍ VA ESTA LÍNEA

            return repository.save(photo);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
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
                log.info("🗑️ Archivo eliminado: {}", path.toAbsolutePath());
            } catch (IOException e) {
                log.warn("⚠️ No se pudo eliminar archivo: {}", e.getMessage());
            }
            repository.delete(photo);
        });
    }
}
