package com.learnboot.dreamshopping.dto;

import com.learnboot.dreamshopping.models.Category;
import com.learnboot.dreamshopping.models.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    private Category category;

    private List<ImageDto> images;
}
