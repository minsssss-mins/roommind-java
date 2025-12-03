package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;
import com.roomgenius.furniture_recommendation.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public int insertComment(CommentDTO dto) {
        return commentMapper.insertComment(dto);
    }

    @Override
    public List<CommentVO> getComments(Integer boardId) {
        return commentMapper.selectCommentsByBoardId(boardId);
    }

    @Override
    public CommentVO getComment(Integer commentId) {
        CommentVO comment = commentMapper.selectCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다. (ID: " + commentId + ")");
        }
        return comment;
    }

    @Override
    public int updateComment(CommentDTO dto) {
        CommentVO origin = commentMapper.selectCommentById(dto.getCommentId());

        if (origin == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다. (ID: " + dto.getCommentId() + ")");
        }

        if (!origin.getUserId().equals(dto.getUserId())) {
            throw new SecurityException("본인의 댓글만 수정할 수 있습니다.");
        }

        return commentMapper.updateComment(dto);
    }

    @Override
    public int deleteComment(Integer commentId, Integer userId) {
        CommentVO origin = commentMapper.selectCommentById(commentId);

        if (origin == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다. (ID: " + commentId + ")");
        }

        if (!origin.getUserId().equals(userId)) {
            throw new SecurityException("본인의 댓글만 삭제할 수 있습니다.");
        }

        return commentMapper.deleteComment(commentId);
    }
}