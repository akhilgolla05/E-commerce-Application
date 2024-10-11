package com.learnboot.dreamshopping.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
