package com.example.shoppingonlybackend.service;

import com.example.shoppingonlybackend.entity.Product;
import com.example.shoppingonlybackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product product) {
        Product oldProduct = productRepository.findById(id).orElse(null);
        if (oldProduct == null) {
            return null;
        }
        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        return productRepository.save(oldProduct);
    }

    public boolean deleteProduct(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return false;
        }
        productRepository.delete(product);
        return true;
    }
    
    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public List<Product> getProductsMoreExpensiveThan(double price) {
        return productRepository.findByPriceGreaterThan(price);
    }
    
    public List<Product> searchProductsByDescription(String keyword) {
        return productRepository.searchByDescription(keyword);
    }    
}
