package com.shop.order_service.controller;

import com.shop.order_service.dto.WishDetailDto;
import com.shop.order_service.dto.WishItemDto;
import com.shop.order_service.dto.WishOrderDto;
import com.shop.order_service.service.WishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order-service")
public class WishController {
    private final WishService wishService;

    @PostMapping("/wish")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid WishItemDto wishItemDto,
                                              BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String id = principal.getName();
        Long wishItemId;

        try {
            wishItemId = wishService.addWish(wishItemDto, id);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @GetMapping("/wish")
    public ResponseEntity<?> orderHist(Principal principal) {
        List<WishDetailDto> wishDetailDtoList = wishService.getWishList(principal.getName());
        return new ResponseEntity<List<WishDetailDto>>(wishDetailDtoList, HttpStatus.OK);
    }

    @PatchMapping("/wishItem/{wishItemId}")
    public @ResponseBody ResponseEntity updateWishItem(@PathVariable("wishItemId") Long wishItemId,
                                                       int wishCount, Principal principal) {
        if (wishCount < 1) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if (!wishService.validateWishItem(wishItemId, principal.getName())) {
            return new ResponseEntity<String>("해당 위시리스트에 담긴 상품이 아니어서 수정권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        wishService.updateWishProductCount(wishItemId, wishCount);
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @DeleteMapping("/wishItem/{wishItemId}")
    public @ResponseBody ResponseEntity deleteWishItem(@PathVariable("wishItemId") Long wishItemId,
                                                       Principal principal) {
        if (!wishService.validateWishItem(wishItemId, principal.getName())) {
            return new ResponseEntity<String>("해당 위시리스트에 담긴 상품이 아니어서 삭제 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        wishService.deleteWishItem(wishItemId);
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @PostMapping("/wish/orders")
    public @ResponseBody ResponseEntity orderWishItem(@RequestBody WishOrderDto wishOrderDto, Principal principal) {
        List<WishOrderDto> wishOrderDtoList = wishOrderDto.getWishOrderDtoList();
        if (wishOrderDtoList == null || wishOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }
        for (WishOrderDto wishOrder : wishOrderDtoList) {
            if (!wishService.validateWishItem(wishOrder.getWishItemId(), principal.getName())) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = wishService.orderWishItem(wishOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}