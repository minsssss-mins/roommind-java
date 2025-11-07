package com.roomgenius.furniture_recommendation.repository;


import com.roomgenius.furniture_recommendation.entity.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insert(BoardVO bvo);


}
