package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class LowStockProductVO {
    private int productId;
    private String name;
    private int stock;

    private Integer originalPrice;
    private Integer salePrice;

}