package com.roomgenius.furniture_recommendation.entity;

import lombok.*;

@Getter  // 각 필드의 getter 메서드 자동 생성
@Setter  // 각 필드의 setter 메서드 자동 생성
@ToString  // 객체 정보를 보기 쉽게 문자열로 변환하는 toString() 자동 생성
@Builder  // 객체 생성 시 빌더 패턴을 자동 생성 (ex: User.builder().name("노아").age(25).build())
@NoArgsConstructor  // 파라미터가 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
public class BoardVO {
    private long bno;
    private String title;
    private String writer;
    private String content;
    private String regAt;
    private String modAt;

}
