package com.learnboot.dreamshopping.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
