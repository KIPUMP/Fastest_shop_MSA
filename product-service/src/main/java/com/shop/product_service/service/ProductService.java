package com.shop.product_service.service;

import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final RedissonClient redissonClient;

    @Cacheable(cacheNames = "PRODUCT", cacheManager = "cacheManager")
    public List<Product> getProductList() {
        List<Product> productList = productRepository.findAll();
        List<Product> newProductList = new ArrayList<>();
        newProductList.addAll(productList);
        return newProductList;
    }

    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));
    }

    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        RLock lock = redissonClient.getLock(productId.toString());
        try {
            boolean isLocked = lock.tryLock(1000, 30, TimeUnit.SECONDS);
            if (isLocked) {
                Product product = productRepository.findProductById(productId).orElseThrow(RuntimeException::new);
                product.removeStock(quantity);
                productRepository.save(product);
            } else {
                System.out.println("상품 재고 수정 중입니다. --- (DB Locked)");
                return;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted" + e);
        } finally {
            lock.unlock();
        }
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }



}