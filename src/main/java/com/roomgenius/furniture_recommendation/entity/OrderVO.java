package com.roomgenius.furniture_recommendation.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVO {

    private Integer orderId;         // order_id
    private Integer userId;          // user_id
    private String deliveryAddress;  // delivery_address
    private String status;           // PENDING / PAID / ...
    private Integer totalPrice;      // total_price

    private LocalDateTime createdDate; // created_date
    private LocalDateTime updatedDate; // updated_date
}
