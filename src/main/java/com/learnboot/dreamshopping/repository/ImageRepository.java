package com.learnboot.dreamshopping.repository;

import com.learnboot.dreamshopping.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(long id);
}
