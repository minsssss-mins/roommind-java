package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class PaymentConfirmDTO {

    private Integer orderId;   // 우리 주문 번호 (Orders.order_id)
    private String paymentId;  // PortOne 결제 아이디 (payment_id)
}
