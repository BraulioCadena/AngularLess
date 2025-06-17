package com.mx.collageamor.service;

import com.mx.collageamor.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    Photo save(MultipartFile file);
    List<Photo> getAll();
    Optional<Photo> getById(Long id);
    void delete(Long id);
}
