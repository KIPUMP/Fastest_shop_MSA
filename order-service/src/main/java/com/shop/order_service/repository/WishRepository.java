package com.shop.order_service.repository;


import com.shop.order_service.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Wish findByUserId(Long userId);
}
