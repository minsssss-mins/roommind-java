package com.roomgenius.furniture_recommendation.service.impl;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.OrderVO;
import com.roomgenius.furniture_recommendation.entity.PaymentConfirmDTO;
import com.roomgenius.furniture_recommendation.mapper.OrderMapper;
import com.roomgenius.furniture_recommendation.service.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderMapper orderMapper;

    @Value("${portone.store-id}")
    private String storeId;

    @Value("${portone.secret}")
    private String secret;

    private static final String PORTONE_BASE_URL = "https://api.portone.io";

    @Override
    @Transactional
    public OrderDTO confirmPayment(Integer userId, PaymentConfirmDTO dto) {

        // 1) 주문 조회 및 소유자 체크
        OrderVO order = orderMapper.selectOrderById(dto.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }
        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("본인 주문만 결제할 수 있습니다.");
        }

        // 2) PortOne 결제 단건 조회 API 호출
        //    GET https://api.portone.io/payments/{paymentId}?storeId={storeId}
        RestTemplate restTemplate = new RestTemplate();

        String url = PORTONE_BASE_URL + "/payments/" + dto.getPaymentId()
                + "?storeId=" + storeId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + secret); // V2 API Secret 사용 :contentReference[oaicite:1]{index=1}
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PortOnePaymentWrapper> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, PortOnePaymentWrapper.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("포트원 결제 정보 조회에 실패했습니다.");
        }

        PortOnePaymentData payment = response.getBody().getData();
        if (payment == null || payment.getAmount() == null) {
            throw new IllegalStateException("포트원 결제 응답이 올바르지 않습니다.");
        }

        int paidAmount = payment.getAmount().getTotal();
        if (!Integer.valueOf(paidAmount).equals(order.getTotalPrice())) {
            throw new IllegalStateException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        // status 값은 문서에서 paid/PAID 스타일이라 가정하고, 대소문자 무시하고 비교
        if (!"PAID".equalsIgnoreCase(payment.getStatus())) {
            throw new IllegalStateException("결제가 완료 상태가 아닙니다. status=" + payment.getStatus());
        }

        // 3) 검증 완료 → 주문 상태 PAID 로 업데이트
        orderMapper.updateOrderStatus(order.getOrderId(), "PAID");

        // 4) 응답용 OrderDTO 생성
        OrderDTO result = new OrderDTO();
        result.setOrderId(order.getOrderId());
        result.setDeliveryAddress(order.getDeliveryAddress());
        result.setTotalPrice(order.getTotalPrice());
        result.setStatus("PAID");
        return result;
    }

    /* ====== PortOne V2 응답 최소 구조용 내부 클래스 ======
       실제 응답 JSON 구조는 Postman/콘솔에서 한 번 찍어보고
       필드명이 다르면 여기만 수정해주면 됨.
    */

    @Data
    private static class PortOnePaymentWrapper {
        private PortOnePaymentData data;
    }

    @Data
    private static class PortOnePaymentData {
        private String id;       // payment_id
        private String status;   // "PAID", "CANCELLED" 등
        private PortOneAmount amount;
    }

    @Data
    private static class PortOneAmount {
        private Integer total;   // 결제 총액
    }
}
