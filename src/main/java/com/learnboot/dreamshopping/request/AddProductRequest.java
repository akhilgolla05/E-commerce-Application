package com.learnboot.dreamshopping.request;

import com.learnboot.dreamshopping.models.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {

    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    private Category category;
}
