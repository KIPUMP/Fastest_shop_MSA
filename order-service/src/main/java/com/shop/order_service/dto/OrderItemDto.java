package com.shop.order_service.dto;

import com.shop.order_service.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemName;
    private int orderCount;
    private int orderPrice;
    public OrderItemDto(OrderItem orderItem) {
        this.itemName = orderItem.getProduct().getProductName();
        this.orderCount = orderItem.getOrderCount();
        this.orderPrice = orderItem.getOrderPrice();
    }
}
