package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;

import java.util.List;

public interface ProductService {
    // 상품 등록
    Integer insertProduct(ProductDTO dto);

    // ⭐ 상품 전체 조회
    List<ProductVO> selectAllProducts();

    // ⭐ 상품 단일 조회 추가
    ProductVO getProductById(Integer productId);

    // ⭐ 단일 삭제 추가
    int deleteProductById(Integer productId);

    // ⭐ 상품 수정 추가
    int updateProduct(ProductDTO dto);
}
