package com.roomgenius.furniture_recommendation.controller;


import com.roomgenius.furniture_recommendation.entity.ReviewDTO;
import com.roomgenius.furniture_recommendation.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /** 상품별 리뷰 조회 */
    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    /** 리뷰 작성 */
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO dto) {
        reviewService.createReview(dto);
        return ResponseEntity.ok("리뷰 작성 완료");
    }

    /** 리뷰 수정 */
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Integer reviewId,
            @RequestBody ReviewDTO dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok("리뷰 수정 완료");
    }

    /** 리뷰 삭제 */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }
}
