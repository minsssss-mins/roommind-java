package com.roomgenius.furniture_recommendation.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;


/**
 * 게시글 DTO (API 요청/응답용)
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QnABoardDTO {

    private Integer qnaBoardId;      // PK
    private Integer userId;          // 작성자 FK

    private String userName;         // JOIN: users.user_name
    private String email;            // JOIN: users.email

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다")
    private String content;
}