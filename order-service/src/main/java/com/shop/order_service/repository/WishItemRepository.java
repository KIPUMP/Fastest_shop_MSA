package com.shop.order_service.repository;

import com.shop.order_service.dto.WishDetailDto;
import com.shop.order_service.entity.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long> {

    WishItem findByWishIdAndProductId(Long wishId, Long productId);

    @Query("SELECT new com.shop.order_service.dto.WishDetailDto(" +
            "    p.id, " +
            "    w.id, "+
            "    p.productName, " +
            "    p.price, " +
            "    w.wishCount) " +
            "FROM WishItem w " +
            "JOIN w.product p " +
            "ON w.product.id = p.id " +
            "WHERE w.wish.id = :id " +
            "ORDER BY w.id DESC")
    List<WishDetailDto> findWishDetailDtoList(@Param("id") Long id);

}