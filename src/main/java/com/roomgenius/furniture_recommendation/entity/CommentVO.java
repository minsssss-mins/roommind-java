package com.roomgenius.furniture_recommendation.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentVO {
    private Integer commentId;
    private Integer communityBoardId;
    private Integer userId;
    private String content;
    private String createdDate;
    private String updatedDate;

    // 유저 정보
    private String userName;
}
