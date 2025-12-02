package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminProductService {

    /** 전체 조회 (필터 없이) */
    List<ProductVO> getAllProducts();

    /** 필터 검색 (검색 + 정렬 + 대분류/중분류) */
    List<ProductVO> selectFilteredProducts(
            String keyword,
            String sort,
            String major,
            String middle
    );

    /** 단일 조회 */
    ProductVO getProductById(Integer id);

    /** 상품 등록 */
    Integer addProduct(ProductDTO dto);

    /** 상품 수정 */
    void updateProduct(Integer id, ProductDTO dto);

    /** 상품 삭제 + 파일 삭제 */
    void deleteProduct(Integer id);

    /** 이미지 업로드 */
    void saveProductImage(Integer productId, MultipartFile file);
}
