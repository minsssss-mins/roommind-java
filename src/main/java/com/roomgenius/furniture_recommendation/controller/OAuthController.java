package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // 🔥 application.yml 의 kakao.* 값을 컨트롤러에서도 사용해야 함
    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    /**
     * 카카오 로그인으로부터 code 받는 엔드포인트
     * → http://localhost:8080/social?code=xxxx
     */
    @GetMapping("/social")
    public ResponseEntity<?> kakaoRedirect(@RequestParam("code") String code) {
        try {
            String jwtToken = oAuthService.loginWithKakao(code);

            // 🔥 JWT 들고 React로 redirect
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:3000/login-success?token=" + jwtToken)
                    .build();

            // JSON 응답 버전 (필요하면 사용)
            // return ResponseEntity.ok().body(
            //     java.util.Map.of("token", jwtToken, "message", "카카오 로그인 성공")
            // );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    java.util.Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * React → Spring → 카카오 로그인 페이지로 리다이렉트
     */
    @GetMapping("/oauth/kakao")
    public void redirectKakao(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&response_type=code";

        response.sendRedirect(url);
    }
}
