package com.shop.product_service.dto;

import com.shop.product_service.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private String productName;
    private String category;
    private String description;
    private int productCount;
    private int price;
    private String productImg;
    private ProductSellStatus productSellStatus;
}
