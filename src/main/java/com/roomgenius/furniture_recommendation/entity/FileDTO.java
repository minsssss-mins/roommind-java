package com.roomgenius.furniture_recommendation.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {

    private String uuid;
    private Integer qnaBoardId;
    private Integer productId;
    private Integer communityBoardId;

    @NotBlank(message = "파일명은 필수입니다")
    private String fileName;

    @Min(value = 0, message = "파일 타입은 0 이상이어야 합니다")
    @Max(value = 1, message = "파일 타입은 1 이하여야 합니다")
    private Integer fileType;

    @Min(value = 0, message = "파일 크기는 0 이상이어야 합니다")
    private Long fileSize;

    private LocalDateTime createdDate;

    // saveDir는 응답에서 제외
}