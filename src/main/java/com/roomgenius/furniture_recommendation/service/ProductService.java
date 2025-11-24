package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;

import java.util.List;

public interface ProductService {
    // 상품 등록
    Integer insertProduct(ProductDTO dto);

    // ⭐ 상품 전체 조회
    List<ProductVO> selectAllProducts();
}
