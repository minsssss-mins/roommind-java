package com.roomgenius.furniture_recommendation.service;

import java.util.Map;

public interface OAuthService {
    
    // 카카오 로그인
    Map<String, Object> loginWithKakao(String code);

    // 네이버 로그인
    Map<String, Object> loginWithNaver(String code, String state);

    // 구글 로그인
    Map<String, Object> loginWithGoogle(String code);

}
