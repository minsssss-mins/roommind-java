package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 댓글 등록
    int insertComment(CommentDTO dto);

    // 특정 게시글 댓글 전체 조회
    List<CommentVO> selectCommentsByBoardId(Integer communityBoardId);

    // 단일 댓글 조회
    CommentVO selectCommentById(Integer commentId);

    // 댓글 수정
    int updateComment(CommentDTO dto);

    // 댓글 삭제
    int deleteComment(Integer commentId);
}
