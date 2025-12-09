package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class ReviewDTO {

    // 요청 시 필요한 값
    private Integer productId;
    private Integer rating;
    private String content;

    // 응답 시 필요한 값
    private Integer reviewId;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    // 마이페이지 리뷰 뽑아오기위한 값! 삭제금지
    private String productName;

}


