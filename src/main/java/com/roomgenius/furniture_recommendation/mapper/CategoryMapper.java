package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryVO> selectAllCategories();

    CategoryVO getCategoryById(Integer categoryId);

    String getCategoryName(Integer categoryId);

    int insertCategory(CategoryVO vo);

    int updateCategory(CategoryVO vo);

    int deleteCategory(Integer categoryId);
}

