package com.mx.collageamor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mx.collageamor.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
