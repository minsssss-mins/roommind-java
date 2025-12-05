package com.roomgenius.furniture_recommendation.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailVO {

    private Integer orderDetailId; // order_detail_id
    private Integer orderId;       // order_id
    private Integer productId;     // product_id
    private Integer price;         // price
    private Integer quantity;      // quantity
}
