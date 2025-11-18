package com.roomgenius.furniture_recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.MemberVO;
import com.roomgenius.furniture_recommendation.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.token_uri}")
    private String kakaoTokenUri;

    @Value("${kakao.user_info_uri}")
    private String kakaoUserInfoUri;


    /** 카카오 로그인 처리 */
    public String loginWithKakao(String code) throws Exception {

        // 1️⃣ Access Token 받기
        String accessToken = getKakaoAccessToken(code);

        // 2️⃣ 사용자 정보 받아오기
        JsonNode userInfo = getKakaoUserInfo(accessToken);

        String socialId = userInfo.get("id").asText();
        String nickname = userInfo.get("properties").get("nickname").asText();

        // 3️⃣ DB 조회
        MemberVO member = memberMapper.findBySocial(socialId, "KAKAO");

        // 4️⃣ 신규 유저라면 DB 저장
        if (member == null) {
            member = MemberVO.builder()
                    .username(nickname)
                    .socialType("KAKAO")
                    .socialId(socialId)
                    .role("user")
                    .build();

            memberMapper.insertMember(member);
        }

        // 5️⃣ JWT 발급
        return jwtTokenProvider.generateToken(member.getEmail(), member.getRole());
    }


    /** 카카오 Access Token 요청 */
    private String getKakaoAccessToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body =
                "grant_type=authorization_code" +
                        "&client_id=" + kakaoClientId +
                        "&redirect_uri=" + kakaoRedirectUri +
                        "&code=" + code;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoTokenUri, entity, String.class);

        JsonNode json = objectMapper.readTree(response.getBody());
        return json.get("access_token").asText();
    }

    /** 카카오 사용자 정보 요청 */
    private JsonNode getKakaoUserInfo(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoUserInfoUri, HttpMethod.GET, entity, String.class);

        return objectMapper.readTree(response.getBody());
    }

}
