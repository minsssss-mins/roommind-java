package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.ProductDTO;
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

}
