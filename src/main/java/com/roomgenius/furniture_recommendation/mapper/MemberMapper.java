package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    // 회원가입 (일반 + 소셜 공용으로 사용)
    int insertMember(MemberVO member);

    // 이메일 중복 체크 (일반 회원가입용)
    int countByEmail(@Param("email") String email);

    // ID로 회원 조회
    MemberVO findById(@Param("userId") Integer userId);

    // 이메일로 회원 조회 (일반 로그인용)
    MemberVO findByEmail(@Param("email") String email);

    // ⭐ 소셜 로그인용: socialId + socialType으로 회원 조회
    MemberVO findBySocial(
            @Param("socialId") String socialId,
            @Param("socialType") String socialType
    );
}
