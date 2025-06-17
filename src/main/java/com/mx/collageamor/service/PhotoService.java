package com.mx.collageamor.service;

import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public Photo save(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String savedFileName = UUID.randomUUID() + "_" + originalFilename;

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("🛠️ Carpeta 'uploads' creada en: " + uploadPath.toAbsolutePath());
            }

            Path filePath = uploadPath.resolve(savedFileName);

            System.out.println("✅ Paso 1: Generando ruta para archivo...");
            System.out.println("📍 Ruta final esperada: " + filePath.toAbsolutePath());

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("✅ Paso 2: Archivo copiado correctamente");

            Photo photo = new Photo();
            photo.setFilename(savedFileName);
            photo.setUrl("/api/photos/download/" + savedFileName);

            System.out.println("📸 Foto registrada: " + photo.getFilename());

            return photoRepository.save(photo);

        } catch (IOException e) {
            System.err.println("❌ Error al guardar archivo: " + e.getMessage());
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }
}
