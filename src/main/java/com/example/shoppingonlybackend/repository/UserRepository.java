package com.example.shoppingonlybackend.repository;

import com.example.shoppingonlybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
