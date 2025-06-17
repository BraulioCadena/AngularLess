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

            Path uploadPath = Paths.get("uploads");
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Photo photo = new Photo();
            photo.setFilename(savedFileName);
            photo.setUrl("/api/photos/download/" + savedFileName); // 👈 esta es la clave

            return photoRepository.save(photo);
        } catch (IOException e) {
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
