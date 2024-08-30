package com.shop.order_service.controller;
import com.shop.order_service.dto.WishDetailDto;
import com.shop.order_service.dto.WishItemDto;
import com.shop.order_service.dto.WishOrderDto;
import com.shop.order_service.service.WishService;
import com.shop.product_service.service.ProductService;
import com.shop.user_service.jwt.JwtUtil;
import com.shop.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @PostMapping("/wish")
    public @ResponseBody ResponseEntity wish(@RequestBody @Valid WishItemDto wishItemDto,
                                              BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<String> ("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        Long wishItemId;
        try {
            wishItemId = wishService.addWish(wishItemDto, userId);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }
    @GetMapping("/wish")
    public ResponseEntity<?> wishHist(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<String>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        List<WishDetailDto> wishDetailDtoList = wishService.getWishList(userId);
        return new ResponseEntity<List<WishDetailDto>>(wishDetailDtoList, HttpStatus.OK);
    }
    @PatchMapping("/wishItem/{wishItemId}")
    public @ResponseBody ResponseEntity updateWishItem(@PathVariable("wishItemId") Long wishItemId,
                                                       int wishCount, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<String>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }

        if (wishCount < 1) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if (!wishService.validateWishItem(wishItemId, userId)) {
            return new ResponseEntity<String>("해당 위시리스트에 담긴 상품이 아니어서 수정권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        wishService.updateWishProductCount(wishItemId, wishCount);
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @DeleteMapping("/wishItem/{wishItemId}")
    public @ResponseBody ResponseEntity deleteWishItem(@PathVariable("wishItemId") Long wishItemId,
                                                       HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<String>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        if (!wishService.validateWishItem(wishItemId, userId)) {
            return new ResponseEntity<String>("해당 위시리스트에 담긴 상품이 아니어서 삭제 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        wishService.deleteWishItem(wishItemId);
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @PostMapping("/wish/orders")
    public @ResponseBody ResponseEntity orderWishItem(@RequestBody WishOrderDto wishOrderDto, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<String>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        List<WishOrderDto> wishOrderDtoList = wishOrderDto.getWishOrderDtoList();
        if (wishOrderDtoList == null || wishOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }
        for (WishOrderDto wishOrder : wishOrderDtoList) {
            if (!wishService.validateWishItem(wishOrder.getWishItemId(), userId)) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
            productService.decreaseStock(wishOrder.getWishItemId(), wishOrder.getWishCount());
        }
        Long orderId = wishService.orderWishItem(wishOrderDtoList, userId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}