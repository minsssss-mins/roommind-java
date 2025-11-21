package com.roomgenius.furniture_recommendation.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {
    private Integer userId;                // PK
    private String userName;           // 이름
    private String phone;              // 전화번호
    private String address;            // 주소

    private String email;              // 일반 로그인에서 사용
    private String password;           // 일반 로그인에서 사용

    private String socialType;         // KAKAO / GOOGLE 등 소셜 로그인 타입
    private String socialId;           // 소셜 계정 고유 ID

    private String role;               // USER / ADMIN
    private LocalDateTime createdDate; // 생성일
    private LocalDateTime updatedDate; // 수정일
}
