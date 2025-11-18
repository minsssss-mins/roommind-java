package com.roomgenius.furniture_recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.MemberVO;
import com.roomgenius.furniture_recommendation.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j // 로그 출력용
@Service // 서비스 계층 클래스
@RequiredArgsConstructor // final 필드를 자동 생성자 주입
public class OAuthService {

    // ⭐ DB 조회/저장하는 MyBatis Mapper
    private final MemberMapper memberMapper;

    // ⭐ JWT 토큰을 생성하는 컴포넌트
    private final JwtTokenProvider jwtTokenProvider;

    // ⭐ JSON String → JsonNode로 파싱하는 Jackson 객체
    private final ObjectMapper objectMapper;

    // ⭐ 외부 API(Kakao) HTTP 요청을 보내는 객체
    private final RestTemplate restTemplate = new RestTemplate();

    // ================================
    //  application.yml에서 가져오는 설정 값
    // ================================
    @Value("${kakao.clientId}")      // ✅ 카멜 케이스
    private String kakaoClientId;

    @Value("${kakao.redirectUri}")   // ✅ 카멜 케이스
    private String kakaoRedirectUri;

    @Value("${kakao.tokenUri}")      // ✅ 카멜 케이스
    private String kakaoTokenUri;

    @Value("${kakao.userInfoUri}")   // ✅ 카멜 케이스
    private String kakaoUserInfoUri;

    // ================================
    //  기본 상수(회원가입 시 사용)
    // ================================
    private static final String SOCIAL_TYPE_KAKAO = "KAKAO"; // 로그인 플랫폼 구분
    private static final String ROLE_USER = "USER"; // 기본 권한
    private static final String DEFAULT_USERNAME = "카카오유저"; // 닉네임 없을 경우 기본값

    /**
     * ⭐ 카카오 로그인 전체 흐름 처리 메서드
     */
    public Map<String, Object> loginWithKakao(String code) {
        try {
            log.info("카카오 로그인 시작 - code: {}", code);

            // 1️⃣ 인가 코드(code) → Access Token 발급
            String accessToken = getKakaoAccessToken(code);
            log.debug("카카오 Access Token 발급 완료");

            // 2️⃣ Access Token으로 카카오 사용자 정보 조회
            JsonNode userInfo = getKakaoUserInfo(accessToken);

            // 3️⃣ JSON에서 필요한 정보 추출
            String socialId = extractValue(userInfo, "id"); // 카카오 고유 ID
            String nickname = extractValue(userInfo, "properties", "nickname");
            String email = extractValue(userInfo, "kakao_account", "email");

            log.info("카카오 사용자 정보 - socialId: {}, email: {}, nickname: {}",
                    socialId, email, nickname);

            // 4️⃣ DB에 소셜 ID로 사용자 조회
            MemberVO member = memberMapper.findBySocial(socialId, SOCIAL_TYPE_KAKAO);

            // 5️⃣ 없으면 회원가입
            if (member == null) {
                log.info("신규 카카오 유저 - 회원가입 진행");

                member = MemberVO.builder()
                        .username(nickname != null ? nickname : DEFAULT_USERNAME) // 닉네임 없으면 기본값
                        .email(email)
                        .socialType(SOCIAL_TYPE_KAKAO)
                        .socialId(socialId)
                        .role(ROLE_USER)
                        .build();

                // DB 저장
                memberMapper.insertMember(member);
                log.info("신규 회원 등록 완료 - userId: {}", member.getUserId());
            } else {
                log.info("기존 카카오 유저 로그인 - userId: {}", member.getUserId());
            }

            // 6️⃣ JWT 토큰을 만들 때 식별자로 사용할 값 선택
            //    - 이메일이 없으면 socialId 사용
            String identifier = member.getEmail() != null
                    ? member.getEmail()
                    : member.getSocialId();

            // 7️⃣ JWT 토큰 생성
            String token = jwtTokenProvider.generateToken(identifier, member.getRole());
            log.info("JWT 토큰 발급 완료");

            // 8️⃣ React로 전달할 로그인 정보 구성
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("username", member.getUsername());
            result.put("socialType", member.getSocialType());
            result.put("role", member.getRole());
            result.put("userId", member.getUserId());

            log.info("로그인 결과 반환 - username: {}, role: {}", member.getUsername(), member.getRole());

            return result;

        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            throw new RuntimeException("카카오 로그인 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * ⭐ 카카오 API에 요청하여 Access Token 받기
     */
    private String getKakaoAccessToken(String code) {
        try {
            // 요청 헤더
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 요청 바디 (카카오 규칙)
            String body = "grant_type=authorization_code"
                    + "&client_id=" + kakaoClientId
                    + "&redirect_uri=" + kakaoRedirectUri
                    + "&code=" + code;

            // POST 요청 엔티티
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // 카카오 토큰 요청
            ResponseEntity<String> response = restTemplate.postForEntity(
                    kakaoTokenUri, entity, String.class
            );

            // 응답 코드 체크
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("카카오 토큰 요청 실패: " + response.getStatusCode());
            }

            // 응답 JSON 파싱
            JsonNode json = objectMapper.readTree(response.getBody());

            // access_token 추출
            if (json.has("access_token")) {
                return json.get("access_token").asText();
            } else {
                throw new RuntimeException("Access Token이 응답에 없습니다");
            }

        } catch (Exception e) {
            log.error("카카오 Access Token 발급 실패", e);
            throw new RuntimeException("카카오 Access Token 발급 실패: " + e.getMessage(), e);
        }
    }

    /**
     * ⭐ Access Token으로 카카오 사용자 정보 가져오기
     */
    private JsonNode getKakaoUserInfo(String accessToken) {
        try {
            // 헤더 (Bearer 토큰 방식)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // 사용자 정보 요청
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoUserInfoUri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("카카오 사용자 정보 요청 실패: " + response.getStatusCode());
            }

            // JSON 파싱
            return objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            throw new RuntimeException("카카오 사용자 정보 조회 실패: " + e.getMessage(), e);
        }
    }

    /**
     * ⭐ JsonNode에서 안전하게 값 추출
     *    예: extractValue(json, "kakao_account", "email");
     */
    private String extractValue(JsonNode node, String... paths) {
        JsonNode current = node;

        for (String path : paths) {
            if (current == null || !current.has(path)) {
                return null; // 경로 없으면 null
            }
            current = current.get(path);
        }

        return current != null && !current.isNull() ? current.asText() : null;
    }
}
