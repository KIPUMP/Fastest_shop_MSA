package com.shop.product_service.entity;
import com.shop.product_service.constant.ProductSellStatus;
import com.shop.product_service.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String category;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int productCount;

    @Column(nullable = false)
    private int price;

    @Column(name = "product_img",columnDefinition = "TEXT")
    private String productImg;

    @Enumerated(EnumType.STRING)
    private ProductSellStatus productSellStatus;


    public Product() {
    }

    public Product(String productName, String category, String description, int productCount, int price, String productImg, ProductSellStatus productSellStatus) {
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.productCount = productCount;
        this.price = price;
        this.productImg = productImg;
        this.productSellStatus = productSellStatus;
    }



    public void removeStock(int orderCount) {
        int stock = this.productCount - orderCount;

        if (stock < 0) {
            throw new OutOfStockException("재고가 부족합니다. 현재 재고 수량" + this.productCount);
        }
        this.productCount = stock;
    }

    public void addstock(int orderCount) {

        this.productCount += orderCount;
    }

    @PreUpdate
    @PrePersist
    public void setProductSellStatus(){
        if (this.productCount== 0) {
            productSellStatus = ProductSellStatus.SOLD_OUT;
        } else {
            productSellStatus = ProductSellStatus.SELL;
        }
    }

}