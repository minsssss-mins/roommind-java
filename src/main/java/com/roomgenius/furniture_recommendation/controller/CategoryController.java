package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import com.roomgenius.furniture_recommendation.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 전체 조회
    @GetMapping
    public List<CategoryVO> list() {
        return categoryService.getAllCategories();
    }

    // 카테고리 단일 조회
    @GetMapping("/{id}")
    public CategoryVO detail(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }



}
