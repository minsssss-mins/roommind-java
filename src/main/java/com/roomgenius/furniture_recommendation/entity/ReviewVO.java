package com.roomgenius.furniture_recommendation.entity;



import lombok.Data;
import java.sql.Timestamp;

@Data
public class ReviewVO {
    private Integer reviewId;
    private Integer userId;
    private Integer productId;
    private Integer rating;       // 1~5
    private String content;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}

