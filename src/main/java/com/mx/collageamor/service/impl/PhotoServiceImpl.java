package com.mx.collageamor.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mx.collageamor.entity.Photo;
import com.mx.collageamor.repository.PhotoRepository;
import com.mx.collageamor.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository repository;
    private final Cloudinary cloudinary;

    @Override
    public Photo save(MultipartFile file) {
        try {
            // Subir imagen a Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String secureUrl = (String) uploadResult.get("secure_url");
            String originalFilename = (String) uploadResult.get("original_filename");

            // Crear y guardar objeto Photo
            Photo photo = new Photo();
            photo.setFilename(originalFilename);
            photo.setUrl(secureUrl);

            System.out.println("✅ Imagen subida exitosamente a Cloudinary: " + secureUrl);
            return repository.save(photo);

        } catch (IOException e) {
            System.err.println("❌ Error al subir imagen a Cloudinary: " + e.getMessage());
            throw new RuntimeException("Error al subir imagen", e);
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

   
}
