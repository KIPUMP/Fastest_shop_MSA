package com.shop.order_service.entity;

import com.shop.product_service.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "order_item")
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_count",nullable = false)
    private int orderCount;

    @Column(name = "order_price",nullable = false)
    private int orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderItem createOrderItem(Product product, int orderCount) {

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrderCount(orderCount);
        orderItem.setOrderPrice();

        product.removeStock(orderCount);
        return orderItem;
    }

    public void setOrderPrice() {
        this.orderPrice = this.product.getPrice() * this.orderCount;
    }

    public void cancel() {
        this.getProduct().addstock(orderCount);

    }

}
