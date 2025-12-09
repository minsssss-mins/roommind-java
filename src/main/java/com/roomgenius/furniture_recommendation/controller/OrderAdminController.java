package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.OrderDetailResponseDTO;
import com.roomgenius.furniture_recommendation.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderAdminController {

    private final OrderService orderService;

    // 전체 주문 조회 (관리자용)
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 상태 변경 (관리자만)
    @PatchMapping("/{orderId}/status")
    public void updateOrderStatus(
            @PathVariable int orderId,
            @RequestBody Map<String, String> request
    ) {
        String newStatus = request.get("status");
        orderService.updateOrderStatus(orderId, newStatus);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public OrderDetailResponseDTO getOrderDetailAdmin(@PathVariable int orderId) {
        return orderService.getOrderDetailAdmin(orderId);
    }

}
