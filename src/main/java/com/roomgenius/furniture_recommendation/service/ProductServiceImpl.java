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
        return productMapper.selectAllProducts();  // 단일 쿼리 실행
    }
}
