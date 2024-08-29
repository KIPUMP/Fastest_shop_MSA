package com.shop.order_service.repository;

import com.shop.order_service.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.email = :email" + " ORDER BY o.orderDate DESC")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.email = :email")
    Long countOrder(@Param("email") String email);

}

