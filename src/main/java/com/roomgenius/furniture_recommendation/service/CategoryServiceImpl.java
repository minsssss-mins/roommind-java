package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import com.roomgenius.furniture_recommendation.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }

    @Override
    public CategoryVO getCategoryById(Integer categoryId) {
        return categoryMapper.getCategoryById(categoryId);
    }

    @Override
    public String getCategoryName(Integer categoryId) {
        return categoryMapper.getCategoryName(categoryId);
    }

    @Override
    public int insertCategory(CategoryVO vo) {
        return categoryMapper.insertCategory(vo);
    }

    @Override
    public int updateCategory(CategoryVO vo) {
        return categoryMapper.updateCategory(vo);
    }

    @Override
    public int deleteCategory(Integer categoryId) {
        return categoryMapper.deleteCategory(categoryId);
    }

    @Override
    public void updateOrder(List<CategoryVO> orderList) {

        for (CategoryVO vo : orderList) {
            categoryMapper.updateOrder(vo.getCategoryId(), vo.getOrderNo());
        }
    }

}

