package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.OrderDTO;
import com.roomgenius.furniture_recommendation.entity.PaymentConfirmDTO;
import com.roomgenius.furniture_recommendation.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 프론트에서 PortOne JS SDK 결제 성공 후
     * paymentId + orderId 를 넘겨주는 엔드포인트
     */
    @PostMapping("/confirm")
    public OrderDTO confirmPayment(
            @RequestAttribute("userId") Integer userId,
            @RequestBody PaymentConfirmDTO dto
    ) {
        return paymentService.confirmPayment(userId, dto);
    }
}
