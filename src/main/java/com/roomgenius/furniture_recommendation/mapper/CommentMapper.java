package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insertComment(CommentDTO dto);

    List<CommentVO> selectCommentsByBoardId(Integer boardId);

    CommentVO selectCommentById(Integer commentId);

    int updateComment(CommentDTO dto);

    int deleteComment(Integer commentId);
}
