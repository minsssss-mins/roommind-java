package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.UserDTO;
import com.roomgenius.furniture_recommendation.entity.UserVO;

public interface UserService {

    // 회원가입
    UserDTO signup(UserDTO dto);

    // 회원 조회
    UserDTO getUserById(Integer userId);

    // 로그인
    UserDTO login(UserDTO dto);

    // ⭐ 이메일로 Member 조회 (QnA 게시판에서 필수)
    UserVO findByEmail(String email);

    UserDTO getUserByEmail(String email);

    UserDTO updateUser(UserDTO dto);

    void changePassword(String email, String currentPw, String newPw);
}