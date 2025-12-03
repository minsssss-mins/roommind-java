package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 회원가입 (일반 + 소셜 공용으로 사용)
    int insertUser(UserVO user);

    // 이메일 중복 체크 (일반 회원가입용)
    int countByEmail(@Param("email") String email);

    // ID로 회원 조회
    UserVO findById(@Param("userId") Integer userId);

    // 이메일로 회원 조회 (일반 로그인용)
    UserVO findByEmail(@Param("email") String email);

    // ⭐ 소셜 로그인용: socialId + socialType으로 회원 조회
    UserVO findBySocial(
            @Param("socialId") String socialId,
            @Param("socialType") String socialType
    );

    void updateUser(UserVO user);

    void updatePassword(UserVO user);
}
