package com.learnboot.dreamshopping.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {

    private long cartId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
}
