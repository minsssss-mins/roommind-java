package com.roomgenius.furniture_recommendation.controller;



import com.roomgenius.furniture_recommendation.service.NaverShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverShoppingController {

    private final NaverShoppingService naverShoppingService;

    /** 🔍 네이버 쇼핑 상품 검색 API */
    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String keyword) {

        // 서비스 호출
        Map result = naverShoppingService.searchProduct(keyword);

        // 응답 반환
        return ResponseEntity.ok(result);
    }
}
