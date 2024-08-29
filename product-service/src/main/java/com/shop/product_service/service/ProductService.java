package com.shop.product_service.service;

import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    // 모든 상품을 가져와서 ProductDto 리스트로 변환하여 반환
    public List<Product> getProductList() {
        List<Product> productList = productRepository.findAll();
        List<Product> newProductList = new ArrayList<>();
        for (Product product : productList) {
            product.setProductSellStatus();
            newProductList.add(product);
        }
        return newProductList;
    }

    // 특정 상품을 가져와서 ProductDto로 변환하여 반환
    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId).orElseThrow(RuntimeException::new);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}