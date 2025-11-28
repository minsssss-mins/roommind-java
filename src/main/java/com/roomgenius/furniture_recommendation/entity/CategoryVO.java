package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class CategoryVO {
    private Integer categoryId;
    private String majorCategory;
    private String middleCategory;
    private String createdDate;
    private String updatedDate;

}
