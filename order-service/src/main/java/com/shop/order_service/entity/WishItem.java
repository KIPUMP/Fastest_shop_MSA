package com.shop.order_service.entity;

import com.shop.product_service.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wish_item")
public class WishItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_item_id")
    private Long id;

    @Column(name = "wish_count")
    @Min(value = 1)
    private int wishCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wish_id")
    private Wish wish;


    public static WishItem createWishItem(Wish wish, Product product, int wishCount) {
        WishItem wishItem = new WishItem();
        wishItem.setWish(wish);
        wishItem.setProduct(product);
        wishItem.setWishCount(wishCount);
        return wishItem;
    }

    public void addCount(int count) {
        this.wishCount += count;
    }

    public void updateCount(int count) {
        this.wishCount = count;
    }

}
