package com.roomgenius.furniture_recommendation.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductDTO {
    private Integer productId;
    private Integer categoryId;
    private String productName;
    private String brand;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer stock;
    private String description;

    // ⭐ 여러 파일 지원 (FileService 구조와 맞춤)
    private List<MultipartFile> files;
}
