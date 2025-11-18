package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j // 로그 출력
@RestController // API 컨트롤러
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 생성
public class OAuthController {

    // ⭐ 카카오 로그인 전체 로직을 처리하는 서비스
    private final OAuthService oAuthService;

    // ===============================
    // yml에서 불러오는 값들
    // ===============================

    @Value("${kakao.clientId}")        // ← client_id → clientId
    private String kakaoClientId;

    @Value("${kakao.redirectUri}")     // ← redirect_uri → redirectUri
    private String kakaoRedirectUri;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;        // ← 이건 그대로 OK

    /**
     * 1️⃣ React가 이 URL 호출 → 카카오 로그인 페이지로 이동
     * GET /oauth/kakao
     */
    @GetMapping("/oauth/kakao")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {

        // ⭐ 카카오 로그인 창 URL (OAuth 규칙)
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoClientId             // 내 앱 REST API 키
                + "&redirect_uri=" + kakaoRedirectUri       // 로그인 성공 후 돌아올 URL
                + "&response_type=code"                     // code 방식 사용
                + "&prompt=select_account";                  // 계정 선택창 강제 표시

        log.info("카카오 로그인 페이지로 리다이렉트: {}", kakaoAuthUrl);

        // ⭐ 사용자 브라우저를 카카오 로그인 페이지로 이동
        response.sendRedirect(kakaoAuthUrl);
    }

    /**
     * 2️⃣ 카카오 로그인 후 카카오가 이 URL로 code 전달
     * GET /social?code=xxxx
     */
    @GetMapping("/social")
    public void handleKakaoCallback(
            @RequestParam("code") String code,  // 카카오가 넘겨주는 인가코드
            HttpServletResponse response
    ) throws IOException {

        try {
            log.info("카카오 콜백 수신 - code: {}", code);

            // ⭐ code로 access_token 받고, 사용자 정보 조회하고, JWT 발급까지 완료
            Map<String, Object> loginResult = oAuthService.loginWithKakao(code);

            // ⭐ OAuthService에서 만들어준 데이터 꺼내기
            String jwtToken = (String) loginResult.get("token");
            String username = (String) loginResult.get("username");
            String socialType = (String) loginResult.get("socialType");
            String role = (String) loginResult.get("role");

            log.info("JWT 토큰 발급 완료");

            // ⭐ React로 넘겨줄 주소 구성 (URL 인코딩 필수!)
            String redirectUrl = frontendUrl + "/login-success"
                    + "?token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8)
                    + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                    + "&socialType=" + socialType
                    + "&role=" + role;

            log.info("React로 리다이렉트: {}", redirectUrl);

            // ⭐ 로그인 성공 후 React로 이동
            response.sendRedirect(redirectUrl);

        } catch (IllegalArgumentException e) {
            // ❗ 카카오에서 이상한 code가 왔을 때
            log.warn("카카오 로그인 실패 - 잘못된 요청: {}", e.getMessage());
            response.sendRedirect(frontendUrl + "/login?error=invalid_request");

        } catch (Exception e) {
            // ❗ 서버 내부 오류
            log.error("카카오 로그인 처리 중 오류 발생", e);
            response.sendRedirect(frontendUrl + "/login?error=server_error");
        }
    }

    /**
     * ︎3️⃣ 네이버 로그인 (아직 구현 안 됨)
     */
    @GetMapping("/oauth/naver")
    public void redirectToNaverLogin(HttpServletResponse response) throws IOException {
        log.info("네이버 로그인 요청 (미구현)");

        // 구현 X → 에러 페이지로 이동
        response.sendRedirect(frontendUrl + "/login?error=not_implemented");
    }

    /**
     * 4️⃣ 구글 로그인 (아직 구현 안 됨)
     */
    @GetMapping("/oauth/google")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        log.info("구글 로그인 요청 (미구현)");

        // 구현 X → 에러 페이지로 이동
        response.sendRedirect(frontendUrl + "/login?error=not_implemented");
    }
}
