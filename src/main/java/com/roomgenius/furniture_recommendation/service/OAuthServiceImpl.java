package com.roomgenius.furniture_recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.UserVO;
import com.roomgenius.furniture_recommendation.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String ROLE_USER = "USER";
    private static final String SOCIAL_TYPE_KAKAO = "KAKAO";
    private static final String SOCIAL_TYPE_NAVER = "NAVER";
    private static final String SOCIAL_TYPE_GOOGLE = "GOOGLE";

    // ===========================
    // 🔹 Kakao 설정 (yml)
    // ===========================
    @Value("${kakao.clientId}")
    private String kakaoClientId;

    @Value("${kakao.redirectUri}")
    private String kakaoRedirectUri;

    @Value("${kakao.tokenUri}")
    private String kakaoTokenUri;

    @Value("${kakao.userInfoUri}")
    private String kakaoUserInfoUri;

    // ===========================
    // 🔹 Naver 설정 (yml)
    // ===========================
    @Value("${naver.clientId}")
    private String naverClientId;

    @Value("${naver.clientSecret}")
    private String naverClientSecret;

    @Value("${naver.redirectUri}")
    private String naverRedirectUri;

    @Value("${naver.tokenUri}")
    private String naverTokenUri;

    @Value("${naver.userInfoUri}")
    private String naverUserInfoUri;

    // ===========================
    // 🔹 Google 설정 (yml)
    // ===========================
    @Value("${google.clientId}")
    private String googleClientId;

    @Value("${google.clientSecret}")
    private String googleClientSecret;

    @Value("${google.redirectUri}")
    private String googleRedirectUri;

    @Value("${google.tokenUri}")
    private String googleTokenUri;

    @Value("${google.userInfoUri}")
    private String googleUserInfoUri;


    // ================================================================
    // ⭐ 카카오 로그인 처리
    // ================================================================
    public Map<String, Object> loginWithKakao(String code) {

        try {
            // 1) 토큰 요청
            String accessToken = getKakaoAccessToken(code);

            // 2) 사용자 정보 요청
            JsonNode userInfo = getKakaoUserInfo(accessToken);

            String socialId = extractValue(userInfo, "id");
            String nickname = extractValue(userInfo, "properties", "nickname");
            String email = extractValue(userInfo, "kakao_account", "email");

            UserVO user = userMapper.findBySocial(socialId, SOCIAL_TYPE_KAKAO);

            if (user == null) {
                user = UserVO.builder()
                        .userName(nickname != null ? nickname : "카카오유저")
                        .email(email)
                        .socialId(socialId)
                        .socialType(SOCIAL_TYPE_KAKAO)
                        .role(ROLE_USER)
                        .build();

                userMapper.insertUser(user);
            }

            // 식별자 (email이 없으면 socialId 사용)
            String identifier = (user.getEmail() != null) ? user.getEmail() : user.getSocialId();
            String jwtToken = jwtTokenProvider.generateToken(identifier, user.getRole());

            Map<String, Object> result = new HashMap<>();
            result.put("token", jwtToken);
            result.put("username", user.getUserName());
            result.put("socialType", user.getSocialType());
            result.put("role", user.getRole());
            result.put("userId", user.getUserId());

            return result;

        } catch (Exception e) {
            throw new RuntimeException("카카오 로그인 오류: " + e.getMessage(), e);
        }
    }

    private String getKakaoAccessToken(String code) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body =
                "grant_type=authorization_code" +
                        "&client_id=" + kakaoClientId +
                        "&redirect_uri=" + kakaoRedirectUri +
                        "&code=" + code;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(kakaoTokenUri, entity, String.class);

        return objectMapper.readTree(response.getBody()).get("access_token").asText();
    }

    private JsonNode getKakaoUserInfo(String accessToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                kakaoUserInfoUri, HttpMethod.GET, entity, String.class
        );

        return objectMapper.readTree(response.getBody());
    }


    // ================================================================
    // ⭐ 네이버 로그인 처리
    // ================================================================
    public Map<String, Object> loginWithNaver(String code, String state) {

        try {
            // 1) AccessToken 요청
            String accessToken = getNaverAccessToken(code, state);

            // 2) 사용자 프로필 요청
            JsonNode userInfo = getNaverUserInfo(accessToken);

            JsonNode res = userInfo.get("response");

            String socialId = extractValue(res, "id");
            String email = extractValue(res, "email");
            String name = extractValue(res, "name");

            UserVO user = userMapper.findBySocial(socialId, SOCIAL_TYPE_NAVER);

            if (user == null) {
                user = UserVO.builder()
                        .userName(name != null ? name : "네이버유저")
                        .email(email)
                        .socialId(socialId)
                        .socialType(SOCIAL_TYPE_NAVER)
                        .role(ROLE_USER)
                        .build();

                userMapper.insertUser(user);
            }

            String identifier = (user.getEmail() != null) ? user.getEmail() : user.getSocialId();
            String jwtToken = jwtTokenProvider.generateToken(identifier, user.getRole());

            Map<String, Object> result = new HashMap<>();
            result.put("token", jwtToken);
            result.put("username", user.getUserName());
            result.put("socialType", user.getSocialType());
            result.put("role", user.getRole());
            result.put("userId", user.getUserId());

            return result;

        } catch (Exception e) {
            throw new RuntimeException("네이버 로그인 오류: " + e.getMessage(), e);
        }
    }

    private String getNaverAccessToken(String code, String state) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body =
                "grant_type=authorization_code" +
                        "&client_id=" + naverClientId +
                        "&client_secret=" + naverClientSecret +
                        "&code=" + code +
                        "&state=" + state;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(naverTokenUri, entity, String.class);

        return objectMapper.readTree(response.getBody()).get("access_token").asText();
    }

    private JsonNode getNaverUserInfo(String accessToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(naverUserInfoUri, HttpMethod.GET, entity, String.class);

        return objectMapper.readTree(response.getBody());
    }

    // ================================================================
    // ⭐ 구글 로그인 처리
    // ================================================================
    public Map<String, Object> loginWithGoogle(String code) {
        try {
            log.info("[Google] AccessToken 요청 시작");

            // 1) AccessToken 요청
            String accessToken = getGoogleAccessToken(code);

            // 2) 사용자 정보 조회
            JsonNode userInfo = getGoogleUserInfo(accessToken);

            String socialId = extractValue(userInfo, "sub");     // Google 고유 ID
            String email    = extractValue(userInfo, "email");
            String username = extractValue(userInfo, "name");

            log.info("[Google] 사용자 정보 - id: {}, email: {}, name: {}", socialId, email, username);

            // 3) DB 조회
            UserVO user = userMapper.findBySocial(socialId, SOCIAL_TYPE_GOOGLE);

            // 4) 없으면 회원가입
            if (user == null) {
                log.info("[Google] 신규 유저 → 회원가입 진행");

                user = UserVO.builder()
                        .userName(username != null ? username : "구글유저")
                        .email(email)
                        .socialType(SOCIAL_TYPE_GOOGLE)
                        .socialId(socialId)
                        .role(ROLE_USER)
                        .build();

                userMapper.insertUser(user);
            }

            // 5) JWT 생성 (email 없으면 socialId 사용)
            String identifier = (email != null) ? email : socialId;
            String token      = jwtTokenProvider.generateToken(identifier, user.getRole());

            // 6) React로 넘겨줄 데이터 구성
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("username", user.getUserName());
            result.put("socialType", user.getSocialType());
            result.put("role", user.getRole());
            result.put("userId", user.getUserId());

            return result;

        } catch (Exception e) {
            log.error("[Google] 로그인 실패", e);
            throw new RuntimeException("Google 로그인 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Google Access Token 발급
     */
    private String getGoogleAccessToken(String code) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body =
                "grant_type=authorization_code" +
                        "&client_id=" + googleClientId +
                        "&client_secret=" + googleClientSecret +
                        "&redirect_uri=" + googleRedirectUri +
                        "&code=" + code;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(googleTokenUri, entity, String.class);

        JsonNode json = objectMapper.readTree(response.getBody());
        return json.get("access_token").asText();
    }

    /**
     * Google 사용자 정보 조회
     */
    private JsonNode getGoogleUserInfo(String accessToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(googleUserInfoUri, HttpMethod.GET, entity, String.class);

        return objectMapper.readTree(response.getBody());
    }




    // ================================================================
    // 공통 JSON 추출 함수
    // ================================================================
    private String extractValue(JsonNode node, String... paths) {

        JsonNode current = node;

        for (String p : paths) {
            if (current == null || !current.has(p)) return null;
            current = current.get(p);
        }

        return current.isNull() ? null : current.asText();
    }



}
