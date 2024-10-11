package com.learnboot.dreamshopping.service.cart;

import com.learnboot.dreamshopping.models.Cart;
import com.learnboot.dreamshopping.models.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(long id);
    void clearCart(long id);
    BigDecimal getTotalPrice(long id);


    Cart initializeNewCart(User user);

    Cart getCartByUserId(long userId);
}
