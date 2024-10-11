package com.learnboot.dreamshopping.request;

import com.learnboot.dreamshopping.models.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    private Category category;
}
