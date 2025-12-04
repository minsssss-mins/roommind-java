package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.ReviewVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    int insert(ReviewVO vo);

    int update(ReviewVO vo);

    int delete(int reviewId);

    ReviewVO findByUserAndProduct(int userId, int productId);

    ReviewVO findById(int reviewId);

    List<ReviewVO> findByProduct(int productId);
}

