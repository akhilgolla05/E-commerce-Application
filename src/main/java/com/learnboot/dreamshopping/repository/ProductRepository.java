package com.learnboot.dreamshopping.repository;

import com.learnboot.dreamshopping.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findProductByBrandAndName(String brand, String name);

    long countByBrandAndName(String brand, String name);

    boolean existsByNameAndBrand(String name, String brand);
}
