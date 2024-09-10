package com.shop.product_service.controller;

import com.shop.product_service.entity.Product;
import com.shop.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-service")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> productList = productService.getProductList();
        return ResponseEntity.ok(productList);

    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @PostMapping("/register")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }
}