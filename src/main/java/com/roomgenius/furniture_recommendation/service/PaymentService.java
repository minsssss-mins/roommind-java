package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.PaymentConfirmDTO;

public interface PaymentService {

    /**
     * 프론트에서 PortOne 결제 성공 후 호출되는 검증 로직
     * - PortOne API로 결제 건 조회
     * - 금액/상태 검증
     * - Orders.status = 'PAID' 로 업데이트
     */
    OrderDTO confirmPayment(Integer userId, PaymentConfirmDTO dto);
}
