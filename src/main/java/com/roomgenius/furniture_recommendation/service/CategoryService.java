package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import java.util.List;

public interface CategoryService {
    List<CategoryVO> getAllCategories();
    CategoryVO getCategoryById(Integer categoryId);
    String getCategoryName(Integer categoryId);

    int insertCategory(CategoryVO vo);
    int updateCategory(CategoryVO vo);
    int deleteCategory(Integer categoryId);
}
