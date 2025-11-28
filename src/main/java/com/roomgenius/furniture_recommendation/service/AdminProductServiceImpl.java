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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductMapper adminProductMapper;
    private final FileService fileService;   // 🔹 파일 처리는 전부 여기로!

    /** ==================== 전체 상품 조회 (이미지 포함) ==================== */
    @Override
    public List<ProductVO> getAllProducts() {
        List<ProductVO> products = adminProductMapper.selectAllProducts();
        attachImages(products);
        return products;
    }

    /** ==================== 필터 상품 조회 (이미지 포함) ==================== */
    @Override
    public List<ProductVO> selectFilteredProducts(Integer categoryId, String keyword, String sort) {
        List<ProductVO> products = adminProductMapper.selectFilteredProducts(categoryId, keyword, sort);
        attachImages(products);
        return products;
    }

    /** ==================== 단일 상품 조회 (이미지 포함) ==================== */
    @Override
    public ProductVO getProductById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }

        ProductVO product = adminProductMapper.getProductById(id);
        if (product == null) {
            throw new NoSuchElementException("존재하지 않는 상품입니다. id=" + id);
        }

        attachImages(product);
        return product;
    }

    /** ==================== 상품 등록 ==================== */
    @Override
    public Integer addProduct(ProductDTO dto) {
        validateProductDTO(dto);

        adminProductMapper.insertProduct(dto);
        Integer productId = adminProductMapper.getLastInsertId();

        // 👉 여기서는 상품 정보만 등록.
        //    이미지 업로드는 별도 saveProductImage() 또는 FileService를 사용하는 컨트롤러에서 처리.

        return productId;
    }

    /** ==================== 상품 수정 ==================== */
    @Override
    public void updateProduct(Integer id, ProductDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }

        validateProductDTO(dto);
        dto.setProductId(id);

        int row = adminProductMapper.updateProduct(dto);
        if (row == 0) {
            throw new RuntimeException("상품 수정에 실패했습니다. id=" + id);
        }
    }

    /** ==================== 상품 삭제 (이미지도 함께 삭제) ==================== */
    @Override
    public void deleteProduct(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }

        // 1) 파일 먼저 삭제 (DB + 물리 파일)
        fileService.deleteProductFiles(id);

        // 2) 상품 삭제
        int row = adminProductMapper.deleteProduct(id);
        if (row == 0) {
            throw new RuntimeException("상품 삭제에 실패했습니다. id=" + id);
        }
    }

    /** ==================== 단일 이미지 업로드 ==================== */
    @Override
    public void saveProductImage(Integer productId, MultipartFile file) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 🔹 실제 저장은 FileService에게 맡김 (중복 X)
        fileService.uploadProductFiles(productId, List.of(file));
    }

    // ==================== 내부 공통 메서드 ====================

    /** 상품 하나에 이미지 붙이기 */
    private void attachImages(ProductVO product) {
        if (product == null || product.getProductId() == null) {
            return;
        }
        List<FileVO> images = fileService.getProductFiles(product.getProductId());
        product.setImages(images);   // ⚠ ProductVO에 List<FileVO> images 필드 있어야 함
    }

    /** 상품 리스트 전체에 이미지 붙이기 */
    private void attachImages(List<ProductVO> products) {
        if (products == null || products.isEmpty()) return;
        for (ProductVO product : products) {
            attachImages(product);
        }
    }

    /** 필수값 검증 */
    private void validateProductDTO(ProductDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("상품 데이터가 없습니다.");
        if (dto.getProductName() == null || dto.getProductName().trim().isEmpty())
            throw new IllegalArgumentException("상품명은 필수입니다.");
        if (dto.getOriginalPrice() == null)
            throw new IllegalArgumentException("상품 가격은 필수입니다.");
        if (dto.getCategoryId() == null)
            throw new IllegalArgumentException("카테고리 ID는 필수입니다.");
    }
}
