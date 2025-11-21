package com.roomgenius.furniture_recommendation.entity;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {
    private Integer productId;
    private Integer categoryId;
    private String productName;
    private String brand;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer stock;
    private String description;
    private String createdDate;
    private String updatedDate;
}