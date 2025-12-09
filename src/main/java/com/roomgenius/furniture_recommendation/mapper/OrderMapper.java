package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    int insertOrder(OrderVO order);

    int insertOrderDetail(OrderDetailVO detail);

    List<OrderVO> selectOrdersByUserId(Integer userId);

    OrderVO selectOrderById(Integer orderId);

    List<OrderDetailVO> selectOrderDetailsByOrderId(Integer orderId);

    // 결제 성공 후 상태 변경용
    int updateOrderStatus(@Param("orderId") Integer orderId,
                          @Param("status") String status);

    // 재고 감소용 (Product 테이블 업데이트)
    int decreaseStock(@Param("productId") Integer productId,
                      @Param("quantity") Integer quantity);

    // 주문 상품 목록 조회 (재고 감소용)
    List<OrderItemDTO> selectOrderItems(Integer orderId);

    List<OrderDTO> selectAllOrdersWithUser();

    List<OrderDetailAdminDTO> selectOrderDetailAdmin(int orderId);


}
