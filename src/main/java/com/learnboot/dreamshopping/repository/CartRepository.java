package com.learnboot.dreamshopping.repository;

import com.learnboot.dreamshopping.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(long userId);
}
