package com.learnboot.dreamshopping.repository;

import com.learnboot.dreamshopping.models.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Transactional
    void deleteAllByCartId(long id);
}
