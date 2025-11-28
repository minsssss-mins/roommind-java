package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import com.roomgenius.furniture_recommendation.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }
}
