package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.OrderDetailResponseDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(Integer userId, OrderDTO dto);

    List<OrderDTO> getUserOrders(Integer userId);

    List<OrderDTO> getAllOrders();

    void updateOrderStatus(int orderId, String newStatus);

    OrderDetailResponseDTO getOrderDetailAdmin(int orderId);
}
