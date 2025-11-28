package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import com.roomgenius.furniture_recommendation.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryVO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
