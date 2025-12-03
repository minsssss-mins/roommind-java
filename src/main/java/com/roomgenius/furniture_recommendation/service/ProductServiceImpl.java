package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import com.roomgenius.furniture_recommendation.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final FileService fileService;

    /* ================================
       🔥 이미지 연결 공통 메서드
    ================================= */
    private void attachImages(ProductVO product) {
        if (product == null) return;
        product.setImages(fileService.getProductFiles(product.getProductId()));
    }

    private void attachImages(List<ProductVO> list) {
        if (list == null) return;
        for (ProductVO p : list) attachImages(p);
    }

    /* 상품 등록 */
    @Override
    @Transactional
    public Integer insertProduct(ProductDTO dto) {

        productMapper.insertProduct(dto);
        Integer productId = dto.getProductId();

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            fileService.uploadProductFiles(productId, dto.getFiles());
        }

        return productId;
    }

    /* 전체 상품 조회 */
    @Override
    public List<ProductVO> selectAllProducts() {
        List<ProductVO> list = productMapper.selectAllProducts();
        attachImages(list);   // ⭐ 이미지 붙이기
        return list;
    }

    /** ===========================
     *  필터 상품 조회 (검색/정렬/대분류/중분류)
     * =========================== */
    @Override
    public List<ProductVO> selectFilteredProducts(String keyword, String sort, String major, String middle) {

        List<ProductVO> products = productMapper.selectFilteredProducts(
                keyword,
                sort,
                major,
                middle
        );

        attachImages(products);
        return products;
    }

    /* 단일 상품 조회 */
    @Override
    public ProductVO getProductById(Integer productId) {
        ProductVO product = productMapper.getProductById(productId);
        attachImages(product);   // ⭐ 이미지 붙이기
        return product;
    }

    /* 상품 삭제 */
    @Override
    public int deleteProductById(Integer productId) {
        return productMapper.deleteProductById(productId);
    }

    /* 상품 수정 + 이미지 수정 */
    @Override
    @Transactional
    public int updateProduct(ProductDTO dto) {

        int result = productMapper.updateProduct(dto);

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {

            fileService.deleteProductFiles(dto.getProductId());
            fileService.uploadProductFiles(dto.getProductId(), dto.getFiles());
        }

        return result;
    }
}
