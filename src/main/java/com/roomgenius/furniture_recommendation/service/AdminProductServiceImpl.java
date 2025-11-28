package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import com.roomgenius.furniture_recommendation.mapper.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductMapper adminProductMapper;

    @Override
    public List<ProductVO> getAllProducts() {
        return adminProductMapper.selectAllProducts();
    }

    @Override
    public List<ProductVO> selectFilteredProducts(Integer categoryId, String keyword, String sort) {
        return adminProductMapper.selectFilteredProducts(categoryId, keyword, sort);
    }

    @Override
    public ProductVO getProductById(Integer id) {
        return adminProductMapper.getProductById(id);
    }

    @Override
    public Integer addProduct(ProductDTO dto) {

        // 1) 상품 DB 저장
        adminProductMapper.insertProduct(dto);

        // 2) 생성된 product_id 조회
        Integer productId = adminProductMapper.getLastInsertId();

        return productId;
    }



    @Override
    public void updateProduct(Integer id, ProductDTO dto) {
        dto.setProductId(id);
        adminProductMapper.updateProduct(dto);
    }

    @Override
    public void deleteProduct(Integer id) {
        adminProductMapper.deleteProduct(id);
    }

    @Override
    public void saveProductImage(Integer productId, MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        try {
            // 1) 날짜 경로 생성
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadFolder = "C:/upload/product/" + datePath;

            File folder = new File(uploadFolder);
            if (!folder.exists()) folder.mkdirs();

            // 2) uuid + 파일명 생성
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + file.getOriginalFilename();

            // 3) 실제 파일 저장
            File saveFile = new File(uploadFolder, saveName);
            file.transferTo(saveFile);

            // 4) DB 저장 객체 생성
            FileVO vo = FileVO.builder()
                    .uuid(uuid)
                    .productId(productId)
                    .saveDir(datePath)
                    .fileName(saveName)
                    .fileSize(file.getSize())
                    .fileType(0)  // 0 = 대표 이미지
                    .build();

            // 5) DB 저장
            adminProductMapper.insertFile(vo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 중 오류 발생");
        }
    }



}
