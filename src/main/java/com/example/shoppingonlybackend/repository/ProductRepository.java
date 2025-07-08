package com.example.shoppingonlybackend.repository;

import com.example.shoppingonlybackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
       List<Product> findByNameContaining(String keyword);

    List<Product> findByPriceGreaterThan(double price);

    @Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword%")
    List<Product> searchByDescriptionByDescription(String keyword);
}
