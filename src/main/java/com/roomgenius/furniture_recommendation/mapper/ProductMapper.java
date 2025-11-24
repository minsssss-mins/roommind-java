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
}

