package com.shop.order_service.service;

import com.shop.order_service.dto.OrderDto;
import com.shop.order_service.dto.WishDetailDto;
import com.shop.order_service.dto.WishItemDto;
import com.shop.order_service.dto.WishOrderDto;
import com.shop.order_service.entity.Wish;
import com.shop.order_service.entity.WishItem;
import com.shop.order_service.repository.WishItemRepository;
import com.shop.order_service.repository.WishRepository;
import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import com.shop.user_service.entity.User;
import com.shop.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final WishItemRepository wishItemRepository;
    private final UserRepository userRepository;

    private final OrderService orderService;

    public Long addWish(WishItemDto wishItemDto, String id) {
        Product product = productRepository.findById(wishItemDto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findByUserId(id).get();

        Wish wish = wishRepository.findByUserId (user.getId());
        if (wish == null) {
            wish = Wish.createWish(user);
            wishRepository.save(wish);
        }
        WishItem savedWishItem = wishItemRepository.findByWishIdAndProductId(wish.getId(), product.getId());

        if (savedWishItem != null) {
            savedWishItem.addCount(wishItemDto.getWishCount());
            return savedWishItem.getId();
        }
        else{
            WishItem wishItem = WishItem.createWishItem(wish, product, wishItemDto.getWishCount());
            wishItemRepository.save(wishItem);
            return wishItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<WishDetailDto> getWishList(String id) {
        List<WishDetailDto> wishDetailDtoList = new ArrayList<>();

        User user = userRepository.findByUserId(id).get();

        Wish wish = wishRepository.findByUserId(user.getId());
        if (wish == null) {
            return wishDetailDtoList;
        }

        wishDetailDtoList = wishItemRepository.findWishDetailDtoList(wish.getId());

        return wishDetailDtoList;

    }

    @Transactional(readOnly = true)
    public boolean validateWishItem(Long wishItemId, String id) {
        User user = userRepository.findByUserId(id).get();
        WishItem wishItem = wishItemRepository.findById(wishItemId).orElseThrow(EntityNotFoundException::new);
        User savedUser = wishItem.getWish().getUser();

        if (!StringUtils.equals(user.getEmail(), savedUser.getEmail())) {
            return false;
        }
        return true;
    }

    public void updateWishProductCount(Long wishItemId, int wishCount) {
        WishItem wishItem = wishItemRepository.findById(wishItemId).orElseThrow(EntityNotFoundException::new);
        wishItem.updateCount(wishCount);
    }

    public void deleteWishItem(Long wishItemId) {
        WishItem wishItem = wishItemRepository.findById(wishItemId).orElseThrow(EntityNotFoundException::new);
        wishItemRepository.delete(wishItem);
    }

    public Long orderWishItem(List<WishOrderDto> wishOrderDtoList, String id) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (WishOrderDto wishOrderDto : wishOrderDtoList) {
            WishItem wishItem = wishItemRepository.findById(wishOrderDto.getWishItemId())
                    .orElseThrow(EntityNotFoundException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setProductId(wishItem.getProduct().getId());
            orderDto.setOrderCount(wishItem.getWishCount());
            orderDtoList.add(orderDto);
        }
        Long orderId = orderService.orders(orderDtoList, id);

        for (WishOrderDto wishOrderDto : wishOrderDtoList) {
            WishItem wishItem = wishItemRepository.findById(wishOrderDto.getWishItemId())
                    .orElseThrow(EntityNotFoundException::new);
            wishItemRepository.delete(wishItem);
        }
        return orderId;
    }

}