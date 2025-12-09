package com.roomgenius.furniture_recommendation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
public class OrderDetailResponseDTO {

    private Integer orderId;
    private String userName;
    private Integer totalPrice;
    private String status;
    private String deliveryAddress;
    private String createdDate;

    private List<Item> items;

    @Data
    @AllArgsConstructor
    public static class Item {
        private Integer orderDetailId;
        private String productName;
        private Integer quantity;
        private Integer price;
        private String imageUrl;
    }
}
