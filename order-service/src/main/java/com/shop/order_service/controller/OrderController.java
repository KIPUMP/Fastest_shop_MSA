package com.shop.order_service.controller;

import com.shop.order_service.dto.OrderDto;
import com.shop.order_service.dto.OrderHistDto;
import com.shop.order_service.service.OrderService;
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


    @PostMapping("/order")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto,
                                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("; ");
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String id = principal.getName();
        Long orderId;
        try {
            orderId = orderService.order(orderDto, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/order", "/order/{page}"})
    public @ResponseBody ResponseEntity<Page<OrderHistDto>> orderHist(@PathVariable("page") Optional<Integer> page, Principal principal) {
        Pageable pageable = PageRequest.of(page.orElse(0), 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        return new ResponseEntity<>(ordersHistDtoList, HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<>("주문자가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    public @ResponseBody ResponseEntity<?> recallOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<>("주문자가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        orderService.recallOrder(orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}