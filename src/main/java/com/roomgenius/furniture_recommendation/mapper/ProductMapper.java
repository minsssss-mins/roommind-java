package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    int insertProduct(ProductDTO dto);

    int insertProductFile(int productId, String saveDir, String fileName, long fileSize);
}

