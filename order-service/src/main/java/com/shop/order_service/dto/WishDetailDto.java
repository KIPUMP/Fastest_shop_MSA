package com.shop.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishDetailDto {
    private Long wishProductId;
    private Long productId;
    private String productName;
    private int price;
    private int wishCount;
    public WishDetailDto(Long wishProductId, Long productId, String productName, int price, int wishCount) {
        this.wishProductId = wishProductId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.wishCount = wishCount;
    }
}
