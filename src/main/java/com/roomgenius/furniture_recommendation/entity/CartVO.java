package com.roomgenius.furniture_recommendation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.security.Timestamp;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartVO {
    private Integer cartId;
    private Integer userId;
    private Integer productId;
    private Integer productCount;
    private String selectedOption;
}


