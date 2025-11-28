package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import com.roomgenius.furniture_recommendation.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryVO> list() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryVO detail(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody CategoryVO vo) {
        categoryService.insertCategory(vo);
        return ResponseEntity.ok("created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestBody CategoryVO vo
    ) {
        vo.setCategoryId(id);
        categoryService.updateCategory(vo);
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("deleted");
    }
}
