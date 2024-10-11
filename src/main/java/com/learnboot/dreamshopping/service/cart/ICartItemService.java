package com.learnboot.dreamshopping.service.cart;

import com.learnboot.dreamshopping.models.CartItem;

public interface ICartItemService {

    void addCartItemToCart(Long cartId, Long productId, int quantity);

    void removeCartItemFromCart(Long cartId, Long productId);

    void updateCartItemQuantity(Long cartId, Long productId, int quantity);


}
