package com.roomgenius.furniture_recommendation.service;



import com.roomgenius.furniture_recommendation.entity.ReviewDTO;
import com.roomgenius.furniture_recommendation.entity.ReviewVO;
import com.roomgenius.furniture_recommendation.mapper.ReviewMapper;
import com.roomgenius.furniture_recommendation.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper; // ⭐ email → userId 조회용 추가

    private Integer getCurrentUserId() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userMapper.findByEmail(email).getUserId();
    }

    private ReviewDTO toDTO(ReviewVO vo) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(vo.getReviewId());
        dto.setUserId(vo.getUserId());
        dto.setProductId(vo.getProductId());
        dto.setRating(vo.getRating());
        dto.setContent(vo.getContent());
        dto.setCreatedDate(vo.getCreatedDate());
        dto.setUpdatedDate(vo.getUpdatedDate());
        return dto;
    }

    /** 리뷰 작성 */
    @Override
    public void createReview(ReviewDTO dto) {

        Integer userId = getCurrentUserId(); // ⭐ 여기서 userId 알아냄

        ReviewVO exists = reviewMapper.findByUserAndProduct(userId, dto.getProductId());
        if (exists != null) {
            throw new RuntimeException("이미 이 상품에 리뷰를 작성했습니다.");
        }

        ReviewVO vo = new ReviewVO();
        vo.setUserId(userId);
        vo.setProductId(dto.getProductId());
        vo.setRating(dto.getRating());
        vo.setContent(dto.getContent());

        reviewMapper.insert(vo);
    }

    /** 리뷰 수정 */
    @Override
    public void updateReview(Integer reviewId, ReviewDTO dto) {

        Integer userId = getCurrentUserId(); // ⭐ 본인 권한 체크

        ReviewVO exist = reviewMapper.findById(reviewId);
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }

        exist.setRating(dto.getRating());
        exist.setContent(dto.getContent());
        reviewMapper.update(exist);
    }

    /** 리뷰 삭제 */
    @Override
    public void deleteReview(Integer reviewId) {

        Integer userId = getCurrentUserId(); // ⭐ 본인 권한 체크

        ReviewVO exist = reviewMapper.findById(reviewId);
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new RuntimeException("리뷰 삭제 권한이 없습니다.");
        }

        reviewMapper.delete(reviewId);
    }

    /** 상품별 리뷰 목록 조회 */
    @Override
    public List<ReviewDTO> getProductReviews(Integer productId) {
        return reviewMapper.findByProduct(productId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer findUserIdByEmail(String email) {
        // UserMapper에서 userId 찾아오도록 구현
        return userMapper.findUserIdByEmail(email);
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Integer userId) {

        return reviewMapper.selectReviewsByUserId(userId)
                .stream()
                .collect(Collectors.toList());
    }



}
