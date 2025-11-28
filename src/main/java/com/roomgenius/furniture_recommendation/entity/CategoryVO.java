package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;

@Data
public class CategoryVO {
    private Integer category_id;
    private String major_category;
    private String middle_category;
    private String created_date;
    private String updated_date;
}
