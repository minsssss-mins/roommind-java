package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;

import java.util.List;

public interface CommentService {

    int insertComment(CommentDTO dto);

    // 특정 게시글 댓글 목록 조회
    List<CommentVO> getComments(Integer communityBoardId);

    // 단일 댓글 조회
    CommentVO getComment(Integer commentId);

    // 댓글 수정
    int updateComment(CommentDTO dto);

    // 댓글 삭제 (본인 확인)
    int deleteComment(Integer commentId, Integer userId);
}
