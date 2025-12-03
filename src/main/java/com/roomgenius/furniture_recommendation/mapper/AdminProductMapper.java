package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {

    /** 전체 조회 */
    List<ProductVO> selectAllProducts();

    /** 필터 조회 (검색, 정렬, 대분류, 중분류) */
    List<ProductVO> selectFilteredProductsAdmin(
            @Param("keyword") String keyword,
            @Param("sort") String sort,
            @Param("major") String major,
            @Param("middle") String middle
    );

    /** 단일 조회 */
    ProductVO getProductById(Integer productId);

    /** 등록 */
    void insertProduct(ProductDTO dto);

    /** 수정 */
    Integer updateProduct(ProductDTO dto);

    /** 삭제 */
    Integer deleteProduct(Integer productId);

    /** 마지막 insert id */
    Integer getLastInsertId();
}
