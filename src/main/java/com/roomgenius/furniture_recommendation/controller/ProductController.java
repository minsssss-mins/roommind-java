package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import com.roomgenius.furniture_recommendation.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /** 상품 정보 등록 **/
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> insertProduct(
            @RequestPart("product") ProductDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        dto.setFiles(files);

        Integer productId = productService.insertProduct(dto);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "productId", productId
        ));
    }

    /**
     * 검색 + 정렬 + 카테고리 필터 + 전체 조회 통합
     * GET /api/admin/products?keyword=&sort=&major=&middle=
     */

    @GetMapping("/filter")
    public List<ProductVO> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String middle
    ) {
        return productService.selectFilteredProducts(keyword, sort, major, middle);
    }

    /** ⭐ 상품 전체 조회 **/
    @GetMapping
    public ResponseEntity<?> selectAllProducts() {
        List<ProductVO> list = productService.selectAllProducts();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", list.size(),
                "products", list
        ));
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Integer productId) {

        ProductVO product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "해당 상품을 찾을 수 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", product
        ));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {

        int result = productService.deleteProductById(productId);

        if (result == 0) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "삭제할 상품을 찾을 수 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "상품이 성공적으로 삭제되었습니다."
        ));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @RequestPart("product") ProductDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        dto.setProductId(productId);
        dto.setFiles(files);

        int updated = productService.updateProduct(dto);

        if (updated == 0) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "수정할 상품을 찾을 수 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "상품이 성공적으로 수정되었습니다."
        ));
    }

}

