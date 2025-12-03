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

    //  CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React 개발 서버 주소
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization")); // JWT 헤더 클라이언트 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // 소셜 로그인 관련 URL 전체 허용
                        .requestMatchers("/oauth/**", "/social/**").permitAll()

                        .requestMatchers("/admin/**").permitAll()

                        //카테고리 crud허용 (관리자 포함)
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/admin/categories/**").permitAll()

                        // 상품 crud 허용  (관리자 포함)
                        .requestMatchers("/api/admin/product/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()

                        // 네이버 검색 api
                        .requestMatchers("/api/naver/**").permitAll()

                        // 회원가입, 로그인
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()

                        // 추가: 이메일로 사용자 정보 조회 허용
                        .requestMatchers("/api/users/email/**").permitAll()

                        // 추가: /api/users?email= 형태도 허용
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()

                        // 게시판 GET은 누구나, 나머지는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/qnaboards/**","/api/community/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/qnaboards/**","/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/qnaboards/**","/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/qnaboards/**","/api/community/**").authenticated()

                        // 카테고리 GET 누구나
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 댓글 GET은 누구나, 나머지는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()

                        // 정적 이미지 허용
                        .requestMatchers("/uploads/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/admin/products/**").permitAll()
                        .requestMatchers(
                                "/", "/admin/**",
                                "/mypage/**", "/community/**",
                                "/popular/**", "/product/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
