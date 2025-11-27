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

    // 에러/성공 리다이렉트 경로 (필요에 따라 수정 가능)
    private static final String LOGIN_SUCCESS_PATH = "/login-success";
    private static final String LOGIN_ERROR_PATH   = "/login-error";

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
     * 2) 카카오 로그인 완료 후 → 카카오가 code 전달
     *    실패/취소 시 error 파라미터 전달 가능
     * 예) /social?code=xxx
     *     /social?error=access_denied&error_description=...
     */
    @GetMapping("/social")
    public void handleKakaoCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(name = "error_description", required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException {

        // 🔥 1) 카카오 로그인 취소 또는 오류
        if (error != null) {
            log.warn("[Kakao] Login canceled or failed: {} - {}", error, errorDescription);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=kakao"
                    + "&reason=cancel";

            response.sendRedirect(redirectUrl);
            return;
        }

        log.info("[Kakao] Callback code = {}", code);

        try {
            // 🔥 2) 정상 로그인 처리
            Map<String, Object> loginResult = oAuthServiceImpl.loginWithKakao(code);

            String redirectUrl = frontendUrl + LOGIN_SUCCESS_PATH
                    + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                    + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                    + "&socialType=" + loginResult.get("socialType")
                    + "&role=" + loginResult.get("role");

            log.info("[Kakao] Redirect to Frontend = {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("[Kakao] 로그인 처리 중 오류", e);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=kakao"
                    + "&reason=error";

            response.sendRedirect(redirectUrl);
        }
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

        // ⭐ CSRF 방지용 state 값 (실제 서비스에서는 세션/Redis 등에 저장해서 검증하는 게 베스트)
        String state = UUID.randomUUID().toString();

        // ⭐ 네이버 로그인 URL 만들기
        String naverAuthUrl =
                "https://nid.naver.com/oauth2.0/authorize"
                        + "?response_type=code"
                        + "&client_id=" + naverClientId
                        + "&redirect_uri=" + naverRedirectUri
                        + "&state=" + state
                        + "&auth_type=reprompt";  // ⭐ 네이버 강제 로그인창

        log.info("[Naver] Redirect URL = {}", naverAuthUrl);

        response.sendRedirect(naverAuthUrl);
    }

    /**
     * 4) 네이버 로그인 완료 후 → 네이버가 code + state 전달
     *    실패/취소 시 error 파라미터 전달
     * GET /social/naver?code=xxx&state=yyy
     * GET /social/naver?error=access_denied&error_description=...&state=...
     */
    @GetMapping("/social/naver")
    public void handleNaverCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(name = "error_description", required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException {

        // 🔥 1) 네이버 로그인 취소 또는 오류 처리
        if (error != null) {
            log.warn("[Naver] Login canceled or failed: {} - {}", error, errorDescription);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=naver"
                    + "&reason=cancel";

            response.sendRedirect(redirectUrl);
            return;
        }

        log.info("[Naver] Callback code = {}, state = {}", code, state);

        try {
            // 🔥 2) 정상 로그인 처리
            Map<String, Object> loginResult = oAuthServiceImpl.loginWithNaver(code, state);

            String redirectUrl = frontendUrl + LOGIN_SUCCESS_PATH
                    + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                    + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                    + "&socialType=" + loginResult.get("socialType")
                    + "&role=" + loginResult.get("role");

            log.info("[Naver] Redirect to Frontend = {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("[Naver] 로그인 처리 중 오류", e);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=naver"
                    + "&reason=error";

            response.sendRedirect(redirectUrl);
        }
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
                        + "&scope=openid%20email%20profile"
                        + "&prompt=select_account"; // ⭐ 구글 계정 선택창

        log.info("[Google] Redirect URL = {}", googleAuthUrl);

        response.sendRedirect(googleAuthUrl);
    }

    /**
     * 구글 로그인 완료 후 콜백
     * GET /social/google?code=xxx
     * 실패/취소 시 /social/google?error=access_denied&error_description=...
     */
    @GetMapping("/social/google")
    public void handleGoogleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(name = "error_description", required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException {

        // 🔥 1) 구글 로그인 취소/오류
        if (error != null) {
            log.warn("[Google] Login canceled or failed: {} - {}", error, errorDescription);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=google"
                    + "&reason=cancel";

            response.sendRedirect(redirectUrl);
            return;
        }

        log.info("[Google] Callback code = {}", code);

        try {
            // 🔥 2) 정상 로그인 처리
            Map<String, Object> loginResult = oAuthServiceImpl.loginWithGoogle(code);

            String redirectUrl = frontendUrl + LOGIN_SUCCESS_PATH
                    + "?token=" + URLEncoder.encode(loginResult.get("token").toString(), StandardCharsets.UTF_8)
                    + "&userName=" + URLEncoder.encode(loginResult.get("username").toString(), StandardCharsets.UTF_8)
                    + "&socialType=" + loginResult.get("socialType")
                    + "&role=" + loginResult.get("role");

            log.info("[Google] Redirect to Frontend = {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("[Google] 로그인 처리 중 오류", e);

            String redirectUrl = frontendUrl + LOGIN_ERROR_PATH
                    + "?provider=google"
                    + "&reason=error";

            response.sendRedirect(redirectUrl);
        }
    }

}
