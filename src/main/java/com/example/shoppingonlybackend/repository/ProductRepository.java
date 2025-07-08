package com.example.shoppingonlybackend.repository;

import com.example.shoppingonlybackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Integer> {

       @Query(value = "SELECT * FROM product WHERE name LIKE %:keyword%", nativeQuery = true)
       List<Product> searchProductsByNameNative(String keyword);
       
       List<Product> findByPriceGreaterThan(Double price);

       @Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword%")
       List<Product> searchByDescription(String keyword);
}
