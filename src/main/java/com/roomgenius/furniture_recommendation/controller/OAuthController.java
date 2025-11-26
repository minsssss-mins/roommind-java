package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.service.OAuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthServiceImpl oAuthServiceImpl;

    // ⭐ 프론트엔드 URL (기본값: http://localhost:3000)
    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    // ========================================================
    // 🔹 카카오 로그인
    // ========================================================

    @Value("${kakao.clientId}")
    private String kakaoClientId;

    @Value("${kakao.redirectUri}")
    private String kakaoRedirectUri;

    /**
     * 1) React → 카카오 로그인 페이지로 이동
     * GET /oauth/kakao
     */
    @GetMapping("/oauth/kakao")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {

        // ⭐ 카카오 로그인 URL 만들기
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&response_type=code"
                + "&prompt=select_account"; // 계정 선택창 자동 표시

        log.info("[Kakao] Redirect URL = {}", kakaoAuthUrl);

        // ⭐ 카카오 로그인 화면으로 이동
        response.sendRedirect(kakaoAuthUrl);
    }

    /**
     * 2) 카카오 로그인 완료 후 → 카카오가 code를 이 URL로 전달
     * GET /social?code=xxxx
     */
    @GetMapping("/social")
    public void handleKakaoCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {

        log.info("[Kakao] Callback code = {}", code);

        // ⭐ code → access_token → 사용자 정보 조회 → JWT 발급
        Map<String, Object> loginResult = oAuthServiceImpl.loginWithKakao(code);

        // ⭐ 프론트로 전달할 값 구성
        String redirectUrl = frontendUrl + "/login-success"
                + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                + "&socialType=" + loginResult.get("socialType")
                + "&role=" + loginResult.get("role");

        log.info("[Kakao] Redirect to Frontend = {}", redirectUrl);

        // ⭐ React로 이동
        response.sendRedirect(redirectUrl);
    }


    // ========================================================
    // 🔹 네이버 로그인
    // ========================================================

    @Value("${naver.clientId}")
    private String naverClientId;

    @Value("${naver.redirectUri}")
    private String naverRedirectUri;

    /**
     * 3) React → 네이버 로그인 페이지로 이동
     * GET /oauth/naver
     */
    @GetMapping("/oauth/naver")
    public void redirectToNaverLogin(HttpServletResponse response) throws IOException {

        // ⭐ CSRF 방지용 state 값
        String state = UUID.randomUUID().toString();

        // ⭐ 네이버 로그인 URL 만들기
        String naverAuthUrl =
                "https://nid.naver.com/oauth2.0/authorize"
                        + "?response_type=code"
                        + "&client_id=" + naverClientId
                        + "&redirect_uri=" + naverRedirectUri
                        + "&state=" + state;

        log.info("[Naver] Redirect URL = {}", naverAuthUrl);

        // ⭐ 네이버 로그인 화면으로 이동
        response.sendRedirect(naverAuthUrl);
    }

    /**
     * 4) 네이버 로그인 완료 후 → 네이버가 code + state 전달
     * GET /social/naver?code=xxx&state=yyy
     */
    @GetMapping("/social/naver")
    public void handleNaverCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {

        log.info("[Naver] Callback code = {}, state = {}", code, state);

        // ⭐ code + state → access_token → 사용자 조회 → JWT 반환
        Map<String, Object> loginResult = oAuthServiceImpl.loginWithNaver(code, state);

        // ⭐ React로 전달할 값 구성
        String redirectUrl = frontendUrl + "/login-success"
                + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                + "&socialType=" + loginResult.get("socialType")
                + "&role=" + loginResult.get("role");

        log.info("[Naver] Redirect to Frontend = {}", redirectUrl);

        // ⭐ React로 이동
        response.sendRedirect(redirectUrl);
    }

    // ===============================
    // ⭐ 구글 로그인
    // ===============================

    @Value("${google.clientId}")
    private String googleClientId;

    @Value("${google.redirectUri}")
    private String googleRedirectUri;

    @Value("${google.authUri}")
    private String googleAuthUri;

    /**
     * React → 구글 로그인 페이지로 이동
     * GET /oauth/google
     */
    @GetMapping("/oauth/google")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {

        // redirect_uri 인코딩 (쿼리스트링에 넣을 때 필수)
        String encodedRedirectUri = URLEncoder.encode(googleRedirectUri, StandardCharsets.UTF_8);

        String googleAuthUrl =
                googleAuthUri
                        + "?client_id=" + googleClientId
                        + "&redirect_uri=" + encodedRedirectUri
                        + "&response_type=code"
                        + "&scope=openid%20email%20profile";

        log.info("[Google] Redirect URL = {}", googleAuthUrl);

        response.sendRedirect(googleAuthUrl);
    }

    /**
     * 구글 로그인 완료 후 콜백
     * GET /social/google?code=xxx
     */
    @GetMapping("/social/google")
    public void handleGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {

        log.info("[Google] Callback code = {}", code);

        Map<String, Object> loginResult = oAuthServiceImpl.loginWithGoogle(code);

        String redirectUrl = frontendUrl + "/login-success"
                + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                + "&socialType=" + loginResult.get("socialType")
                + "&role=" + loginResult.get("role");

        log.info("[Google] Redirect to Frontend = {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }




}
