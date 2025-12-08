package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.service.OrderService;
import com.roomgenius.furniture_recommendation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    // 주문 생성 (PENDING)
    @PostMapping
    public OrderDTO createOrder(
            @AuthenticationPrincipal String email,
            @RequestBody OrderDTO dto
    ) {
        Integer userId = userService.findIdByEmail(email);
        return orderService.createOrder(userId, dto);
    }


    // 내주문목록조회
    @GetMapping
    public List<OrderDTO> getMyOrders(
            @AuthenticationPrincipal String email
    ) {
        Integer userId = userService.findIdByEmail(email);
        return orderService.getUserOrders(userId);
    }
}






