package com.roomgenius.furniture_recommendation.service.impl;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.OrderItemDTO;
import com.roomgenius.furniture_recommendation.entity.OrderVO;
import com.roomgenius.furniture_recommendation.entity.OrderDetailVO;
import com.roomgenius.furniture_recommendation.mapper.OrderMapper;
import com.roomgenius.furniture_recommendation.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(Integer userId, OrderDTO dto) {

        // 🛡️ 방어 코드: items 비었으면 예외
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
        }

        // 총 금액 계산
        int totalPrice = dto.getItems().stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();

        // Orders INSERT (PENDING)
        OrderVO order = OrderVO.builder()
                .userId(userId)
                .deliveryAddress(dto.getDeliveryAddress())
                .status("PENDING")
                .totalPrice(totalPrice)
                .build();

        orderMapper.insertOrder(order); // orderId 세팅됨

        // Order_Detail INSERT
        for (OrderItemDTO item : dto.getItems()) {
            OrderDetailVO detail = OrderDetailVO.builder()
                    .orderId(order.getOrderId())
                    .productId(item.getProductId())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .build();

            orderMapper.insertOrderDetail(detail);
        }

        // 응답 DTO 세팅
        dto.setOrderId(order.getOrderId());
        dto.setTotalPrice(totalPrice);
        dto.setStatus("PENDING");

        return dto;
    }

    @Override
    public List<OrderDTO> getUserOrders(Integer userId) {

        List<OrderVO> orders = orderMapper.selectOrdersByUserId(userId);
        List<OrderDTO> result = new ArrayList<>();

        for (OrderVO o : orders) {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(o.getOrderId());
            dto.setDeliveryAddress(o.getDeliveryAddress());
            dto.setTotalPrice(o.getTotalPrice());
            dto.setStatus(o.getStatus());
            result.add(dto);
        }

        return result;
    }
}
