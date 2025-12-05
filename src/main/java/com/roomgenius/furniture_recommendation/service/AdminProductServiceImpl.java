package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import com.roomgenius.furniture_recommendation.mapper.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductMapper adminProductMapper;
    private final FileService fileService;

    /** 전체 상품 조회 */
    @Override
    public List<ProductVO> getAllProducts() {
        List<ProductVO> products = adminProductMapper.selectAllProducts();
        attachImages(products);
        return products;
    }

    /** ===========================
     *  필터 상품 조회 (검색/정렬/대분류/중분류)
     * =========================== */
    @Override
    public List<ProductVO> selectFilteredProducts(String keyword, String sort, String major, String middle) {

        List<ProductVO> products = adminProductMapper.selectFilteredProductsAdmin(
                keyword,
                sort,
                major,
                middle
        );

        attachImages(products);
        return products;
    }

    /** 단일 상품 조회 */
    @Override
    public ProductVO getProductById(Integer id) {
        if (id == null) throw new IllegalArgumentException("상품 ID는 필수입니다.");

        ProductVO product = adminProductMapper.getProductById(id);
        if (product == null) throw new NoSuchElementException("존재하지 않는 상품입니다. id=" + id);

        attachImages(product);
        return product;
    }

    /** 상품 등록 */
    @Override
    public Integer addProduct(ProductDTO dto) {

        validateProductDTO(dto);

        adminProductMapper.insertProduct(dto);
        Integer productId = adminProductMapper.getLastInsertId();

        return productId;
    }

    /* 상품 수정 + 이미지 수정 */
    @Override
    @Transactional
    public int updateProduct(ProductDTO dto) {

        int result = adminProductMapper.updateProduct(dto);

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {

            fileService.deleteProductFiles(dto.getProductId());
            fileService.uploadProductFiles(dto.getProductId(), dto.getFiles());
        }

        return result;
    }

    /** 상품 삭제 + 파일 삭제 */
    @Override
    public void deleteProduct(Integer id) {
        if (id == null) throw new IllegalArgumentException("상품 ID는 필수입니다.");

        fileService.deleteProductFiles(id);

        int row = adminProductMapper.deleteProduct(id);
        if (row == 0) throw new RuntimeException("상품 삭제 실패 id=" + id);
    }

    /** 이미지 저장 */
    @Override
    public void saveProductImage(Integer productId, MultipartFile file) {
        if (productId == null) throw new IllegalArgumentException("상품 ID는 필수입니다.");
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("파일 없음");

        fileService.uploadProductFiles(productId, List.of(file));
    }


    /* 이미지 연결 */
    private void attachImages(ProductVO product) {
        if (product == null) return;
        List<FileVO> images = fileService.getProductFiles(product.getProductId());
        product.setImages(images);
    }

    private void attachImages(List<ProductVO> list) {
        if (list == null) return;
        for (ProductVO p : list) {
            attachImages(p);
        }
    }

    /* 필수값 검증 */
    private void validateProductDTO(ProductDTO dto) {
        if (dto == null) throw new IllegalArgumentException("상품 데이터 없음");
        if (dto.getProductName() == null || dto.getProductName().isBlank())
            throw new IllegalArgumentException("상품명 필수");
        if (dto.getOriginalPrice() == null)
            throw new IllegalArgumentException("가격 필수");
        if (dto.getCategoryId() == null)
            throw new IllegalArgumentException("카테고리 ID 필수");
    }
}



