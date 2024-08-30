package com.shop.product_service.service;

import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RedissonClient redissonClient;


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
    @Transactional
    public synchronized void decreaseStock(Long productId, int quantity){
        RLock lock = redissonClient.getLock(productId.toString());
        try{
            if(lock.tryLock(1,5, TimeUnit.SECONDS)){
                Product product = productRepository.findProductById(productId).orElseThrow(RuntimeException::new);
                product.removeStock(quantity);
                productRepository.save(product);
            }
            else{
                throw new RuntimeException("상품에 접근하여 잠금 중입니다");
            }
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted" + e);
        }finally {
            lock.unlock();
        }
    }
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}