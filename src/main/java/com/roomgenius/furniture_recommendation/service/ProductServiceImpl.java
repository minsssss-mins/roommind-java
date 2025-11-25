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
    private final FileService fileService;   // ⭐ 파일 처리 완전 통합

    @Override
    @Transactional
    public Integer insertProduct(ProductDTO dto) {

        // 1) 상품 저장
        productMapper.insertProduct(dto);
        Integer productId = dto.getProductId();

        // 2) 파일 저장 (⭐ FileServiceImpl 사용)
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            fileService.uploadProductFiles(productId, dto.getFiles());
        }

        return productId;
    }

    @Override
    public List<ProductVO> selectAllProducts() {
        return productMapper.selectAllProducts();
    }

    // 단일조회 메서드 추가 (오류 해결의 핵심)
    @Override
    public ProductVO getProductById(Integer productId) {
        return productMapper.getProductById(productId);
    }

    @Override
    public int deleteProductById(Integer productId) {
        return productMapper.deleteProductById(productId);
    }

    @Override
    @Transactional
    public int updateProduct(ProductDTO dto) {

        int result = productMapper.updateProduct(dto);

        // 파일이 있을 경우 파일도 수정 처리
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {

            // 기존 파일 삭제 (DB / 로컬 모두)
            fileService.deleteProductFiles(dto.getProductId());

            // 새로운 파일 업로드
            fileService.uploadProductFiles(dto.getProductId(), dto.getFiles());
        }

        return result;
    }
}
