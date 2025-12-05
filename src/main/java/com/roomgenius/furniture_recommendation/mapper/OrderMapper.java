package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.OrderVO;
import com.roomgenius.furniture_recommendation.entity.OrderDetailVO;
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

    // 🔥 결제 성공 후 상태 변경용
    int updateOrderStatus(@Param("orderId") Integer orderId,
                          @Param("status") String status);
}
