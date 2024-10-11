package com.learnboot.dreamshopping.dto;

import com.learnboot.dreamshopping.models.Cart;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    private List<OrderDto> orders;
    private CartDto cart;
}
