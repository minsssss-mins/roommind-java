package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminProductMapper {

    List<ProductVO> selectAllProducts();

    List<ProductVO> selectFilteredProducts(Integer categoryId, String keyword, String sort);

    ProductVO getProductById(Integer productId);

    void insertProduct(ProductDTO dto);

    Integer updateProduct(ProductDTO dto);

    Integer deleteProduct(Integer productId);

    Integer getLastInsertId();

    void insertFile(FileVO vo);

    List<FileVO> selectFilesByProductId(Integer productId);
}
