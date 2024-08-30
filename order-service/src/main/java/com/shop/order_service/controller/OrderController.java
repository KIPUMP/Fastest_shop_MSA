package com.shop.order_service.controller;

import com.shop.order_service.dto.OrderDto;
import com.shop.order_service.dto.OrderHistDto;
import com.shop.order_service.service.OrderService;
import com.shop.user_service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order-service")
public class OrderController {
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        // Validate the orderDto
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("; ");
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        // Extract user ID from JWT token
        String userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        // Process the order
        Long orderId;
        try {
            orderId = orderService.order(orderDto, userId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/order", "/order/{page}"})
    public @ResponseBody ResponseEntity<Page<OrderHistDto>> orderHist(@PathVariable("page") Optional<Integer> page, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page.orElse(0), 4);

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        // Extract user ID from JWT token
        String userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(userId, pageable);

        return new ResponseEntity<>(ordersHistDtoList, HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<>("No JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String userId;
        try{
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        if (!orderService.validateOrder(orderId, userId)) {
           return new ResponseEntity<>("주문자가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/recall")
    public @ResponseBody ResponseEntity<?> recallOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<>("NO JWT token found in request headers", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        if (!orderService.validateOrder(orderId, userId)) {
            return new ResponseEntity<>("주문자가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        orderService.recallOrder(orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

}