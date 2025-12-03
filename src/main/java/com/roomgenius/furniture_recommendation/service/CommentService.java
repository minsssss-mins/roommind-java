package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;

import java.util.List;

public interface CommentService {

    int insertComment(CommentDTO dto);

    List<CommentVO> getComments(Integer boardId);

    CommentVO getComment(Integer commentId);

    int updateComment(CommentDTO dto);

    int deleteComment(Integer commentId, Integer userId);
}
