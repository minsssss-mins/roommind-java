package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class OrderDetailAdminDTO {
    private Integer orderId;
    private Integer userId;
    private String userName;

    private Integer totalPrice;
    private String status;
    private String deliveryAddress;
    private String createdDate;

    private Integer orderDetailId;
    private Integer productId;
    private String productName;
    private Integer price;
    private Integer quantity;

    private String imageUrl;
}
