package com.shop.order_service.entity;

import com.shop.order_service.constant.OrderStatus;
import com.shop.user_service.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(User user, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setUser(user);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        if (orderDate.isBefore(LocalDateTime.now().minusDays(1))) {
            throw new IllegalStateException("배송이 시작된 상품은 취소가 불가능합니다.");
        } else {
            this.orderStatus = OrderStatus.CANCEL;

        }
    }

    public void changeOrderStatus() {
        if(this.orderStatus == OrderStatus.RECALL && orderDate.isBefore(LocalDateTime.now().minusDays(4))) {
            this.orderStatus = OrderStatus.RECALL_COMPLETE;
            for (OrderItem orderItem : orderItems) {
                orderItem.cancel();
            }
        }
        else if (orderDate.isBefore(LocalDateTime.now().minusDays(2))) {
            this.orderStatus = OrderStatus.COMPLETE;
        } else if (orderDate.isBefore(LocalDateTime.now().minusDays(1))) {
            this.orderStatus = OrderStatus.DELIVERY;
        } else {
            this.orderStatus = OrderStatus.ORDER;
        }
    }

    public void recallOrder() {
        if(this.orderStatus == OrderStatus.COMPLETE && orderDate.isBefore(LocalDateTime.now().minusDays(3))) {
            this.orderStatus = OrderStatus.RECALL;
        } else {
            throw new IllegalStateException("배송 후 익일이 지났으므로 반품 불가입니다");
        }

    }

}