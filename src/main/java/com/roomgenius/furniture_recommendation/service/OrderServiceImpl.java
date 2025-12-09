package com.roomgenius.furniture_recommendation.service.impl;

import com.roomgenius.furniture_recommendation.entity.*;
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

        // 방어 코드
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
        }

        // 총 금액 계산
        int totalPrice = dto.getItems().stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();

        // Orders INSERT
        OrderVO order = OrderVO.builder()
                .userId(userId)
                .deliveryAddress(dto.getDeliveryAddress())
                .status("PENDING")
                .totalPrice(totalPrice)
                .build();

        orderMapper.insertOrder(order);

        // 상세 INSERT
        for (OrderItemDTO item : dto.getItems()) {
            OrderDetailVO detail = OrderDetailVO.builder()
                    .orderId(order.getOrderId())
                    .productId(item.getProductId())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .build();

            orderMapper.insertOrderDetail(detail);
        }

        // 응답 설정
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

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderMapper.selectAllOrdersWithUser();
    }

    @Override
    public void updateOrderStatus(int orderId, String newStatus) {
        orderMapper.updateOrderStatus(orderId, newStatus);
    }

    // 관리자: 주문 상세 조회
    @Override
    public OrderDetailResponseDTO getOrderDetailAdmin(int orderId) {

        List<OrderDetailAdminDTO> rows = orderMapper.selectOrderDetailAdmin(orderId);

        if (rows.isEmpty()) return null;

        OrderDetailAdminDTO first = rows.get(0);

        OrderDetailResponseDTO dto = new OrderDetailResponseDTO();
        dto.setOrderId(first.getOrderId());
        dto.setUserName(first.getUserName());
        dto.setTotalPrice(first.getTotalPrice());
        dto.setStatus(first.getStatus());
        dto.setDeliveryAddress(first.getDeliveryAddress());
        dto.setCreatedDate(first.getCreatedDate());

        List<OrderDetailResponseDTO.Item> items =
                rows.stream()
                        .map(r -> new OrderDetailResponseDTO.Item(
                                r.getOrderDetailId(),
                                r.getProductName(),
                                r.getQuantity(),
                                r.getPrice(),
                                r.getImageUrl()   // ★ URL 가공 없음
                        ))
                        .toList();

        dto.setItems(items);

        return dto;
    }
}
