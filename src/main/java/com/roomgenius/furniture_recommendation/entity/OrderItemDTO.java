package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class OrderItemDTO {

    private Integer productId;
    private Integer price;
    private Integer quantity;
}
