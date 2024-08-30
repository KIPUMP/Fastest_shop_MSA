package com.shop.order_service.service;

import com.shop.order_service.dto.OrderDto;
import com.shop.order_service.dto.OrderHistDto;
import com.shop.order_service.dto.OrderItemDto;
import com.shop.order_service.entity.Order;
import com.shop.order_service.entity.OrderItem;
import com.shop.order_service.repository.OrderRepository;
import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import com.shop.user_service.entity.User;
import com.shop.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String id) {

        Product product = productRepository.findById(orderDto.getProductId()).orElseThrow(()
                -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));
        Optional<User> user = userRepository.findByUserId(id);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(product, orderDto.getOrderCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(user.get(), orderItemList);
        order.changeOrderStatus();
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public Page<OrderHistDto> getOrderList(String id, Pageable pageable) {
        List<Order> orders = (List<Order>) orderRepository.findOrders(id, pageable);
        Long totalCount = orderRepository.countOrder(id);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for (Order order : orders) {
            order.changeOrderStatus();
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                OrderItemDto orderItemDto = new OrderItemDto(orderItem);
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtoList.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtoList, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String id) {
        User user = userRepository.findByUserId(id).orElseThrow(EntityNotFoundException::new);

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        User saveUser = order.getUser();

        if (!StringUtils.equals(user.getEmail(), saveUser.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        try {
            order.cancelOrder();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
        List<OrderItem> cancelOrderList = order.getOrderItems();
        for (OrderItem orderItem : cancelOrderList) {
            orderItem.cancel();
        }
    }


    public Long orders(List<OrderDto> orderDtoList, String id) {
        User user = userRepository.findByUserId(id).orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Product product = productRepository.findById(orderDto.getProductId()).orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem(product, orderDto.getOrderCount());
            orderItemList.add(orderItem);

        }
        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    public void recallOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        try {
            order.recallOrder();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

}