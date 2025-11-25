package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    int insertProduct(ProductDTO dto);

    int insertProductFile(int productId, String saveDir, String fileName, long fileSize);

    // ⭐ 전체 상품 조회
    List<ProductVO> selectAllProducts();

    // ⭐ 단일 상품 조회 추가
    ProductVO getProductById(Integer productId);

    // ⭐ 단일 삭제
    int deleteProductById(Integer productId);

    // ⭐ 수정 추가
    int updateProduct(ProductDTO dto);
}


