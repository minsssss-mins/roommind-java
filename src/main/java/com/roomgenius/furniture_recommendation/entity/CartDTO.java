package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class CartDTO {

    private Integer cartId;
    private Integer userId;
    private Integer productId;
    private Integer productCount;
    private String selectedOption;

    // 본인 확인용 email
    private String email;

    // 조회 시 포함될 상품 정보
    private String productName;
    private Integer price;
    private String imageUrl;
}
