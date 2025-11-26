package com.roomgenius.furniture_recommendation.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 VO (DB 조회 결과)
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnABoardVO {
    private Integer qnaBoardId;    // PK: qna_board_id
    private Integer userId;        // FK: user_id (User 테이블)

    // 🔹 JOIN 시 가져올 수 있는 추가 정보 (DB 컬럼 아님)
    private String userName;
    private String email;

    private String title;          // 제목
    private String content;        // 내용

    private LocalDateTime createdDate;  // created_date
    private LocalDateTime updatedDate;  // updated_date

    private List<FileVO> images;
}