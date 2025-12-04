package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.ReviewDTO;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDTO dto);
    void updateReview(Integer reviewId, ReviewDTO dto);
    void deleteReview(Integer reviewId);
    List<ReviewDTO> getProductReviews(Integer productId);
}


