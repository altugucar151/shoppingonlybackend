package com.example.shoppingonlybackend.repository;

import com.example.shoppingonlybackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Integer> {

}
