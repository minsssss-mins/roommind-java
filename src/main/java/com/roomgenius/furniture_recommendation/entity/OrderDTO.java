package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    // ▼ 요청용
    private String deliveryAddress;      // 배송지 전체 문자열
    private List<OrderItemDTO> items;    // 주문 상품 목록

    // ▼ 응답용
    private Integer orderId;             // 생성된 주문 번호
    private Integer totalPrice;          // 서버에서 최종 계산한 총액
    private String status;               // PENDING / PAID ...

    private int userId;       // mapper에서 select함
    private String userName;  // mapper에서 select함

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;



}
