package com.mx.collageamor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import com.mx.collageamor.entity.Photo;

public interface PhotoService {
    Photo save(MultipartFile file);
    List<Photo> getAll();
    Optional<Photo> getById(Long id);
    void delete(Long id);
}
