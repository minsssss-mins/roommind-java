package com.roomgenius.furniture_recommendation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React 개발 서버 주소
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization")); // JWT 헤더 클라이언트 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CORS 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ CSRF 비활성화 (REST API용)
                .csrf(csrf -> csrf.disable())

                // ✅ 세션 비활성화 (JWT 방식)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 소셜로그인 누구나 접근가능
                        .requestMatchers("/social").permitAll()
                        .requestMatchers("/oauth/kakao").permitAll()
                        .requestMatchers("/api/naver/**").permitAll()
                        // 회원가입, 로그인은 누구나 접근 가능
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()

                        // 게시판: 조회(GET)은 공개, 등록/수정/삭제는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/qnaboards/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/qnaboards/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/qnaboards/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/qnaboards/**").authenticated()

                        // 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 그 외 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // ✅ JWT 필터 등록 (UsernamePasswordAuthenticationFilter 앞에 추가)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
