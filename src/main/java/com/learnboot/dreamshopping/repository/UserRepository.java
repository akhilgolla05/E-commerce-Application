package com.learnboot.dreamshopping.repository;

import com.learnboot.dreamshopping.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String username);

}
