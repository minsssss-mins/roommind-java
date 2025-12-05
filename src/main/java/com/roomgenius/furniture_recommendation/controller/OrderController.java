package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성 (PENDING)
    @PostMapping
    public OrderDTO createOrder(
            @RequestAttribute("userId") Integer userId,
            @RequestBody OrderDTO dto
    ) {
        return orderService.createOrder(userId, dto);
    }

    // 내 주문 목록 조회
    @GetMapping
    public List<OrderDTO> getMyOrders(
            @RequestAttribute("userId") Integer userId
    ) {
        return orderService.getUserOrders(userId);
    }
}
