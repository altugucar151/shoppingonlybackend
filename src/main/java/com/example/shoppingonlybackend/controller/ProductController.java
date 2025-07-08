package com.example.shoppingonlybackend.controller;

import com.example.shoppingonlybackend.entity.Product;
import com.example.shoppingonlybackend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Member veya Admin erişebilir
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Member veya Admin erişebilir
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    // Sadece Admin veya POST_ADMIN yetkisi olan erişebilir
    @PostMapping
    public ResponseEntity<?> create(Principal altug, @RequestBody Product product) {

        System.out.println("Kim bu? principal.getName(): " + altug.getName());

        Product newProduct = productService.createProduct(product);
        return ResponseEntity.ok(newProduct);
    }

    // Sadece Admin erişebilir
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PUT_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    // Sadece Admin erişebilir
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/searchByName")
    public ResponseEntity<?> searchByName(@RequestParam String keyword) {

        List<Product> products = productService.searchProductsByName(keyword);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/moreExpensiveThan")
    public ResponseEntity<?> getProductsMoreExpensiveThan(@RequestParam double price) {
        List<Product> products = productService.getProductsMoreExpensiveThan(price);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/searchByDescription")
    public ResponseEntity<?> searchByDescription(@RequestParam String keyword) {
        List<Product> products = productService.searchProductsByDescription(keyword);
        return ResponseEntity.ok(products);
    }
}
