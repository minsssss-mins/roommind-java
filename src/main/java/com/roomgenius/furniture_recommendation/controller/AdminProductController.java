package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
import com.roomgenius.furniture_recommendation.entity.ProductVO;
import com.roomgenius.furniture_recommendation.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    /** 리스트 조회 + 검색/정렬/필터 */
    @GetMapping
    public List<ProductVO> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String middle
    ) {
        return adminProductService.selectFilteredProducts(keyword, sort, major, middle);
    }

    /** 단일 조회 */
    @GetMapping("/{id}")
    public ProductVO getOne(@PathVariable Integer id) {
        return adminProductService.getProductById(id);
    }

    /** ==========================
     *  상품 등록 (상품 + 단일 이미지)
     * ========================== */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> insert(
            @RequestPart("product") ProductDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        // 1) 상품 저장
        Integer productId = adminProductService.addProduct(dto);

        // 2) 이미지 저장
        if (file != null && !file.isEmpty()) {
            adminProductService.saveProductImage(productId, file);
        }

        return ResponseEntity.ok("created");
    }

    /** ==========================
     *  상품 수정 (상품 JSON + 이미지들)
     * ========================== */
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @RequestPart("product") ProductDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        dto.setProductId(productId);
        dto.setFiles(files);

        int updated = adminProductService.updateProduct(dto);

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

    /** 삭제 + 이미지 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.ok("deleted");
    }
}
