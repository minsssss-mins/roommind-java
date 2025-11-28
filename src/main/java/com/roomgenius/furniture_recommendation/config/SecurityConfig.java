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
                //  CORS 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                //  CSRF 비활성화 (REST API용)
                .csrf(csrf -> csrf.disable())

                //  세션 비활성화 (JWT 방식)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //  URL별 권한 설정
                .authorizeHttpRequests(auth -> auth


                        //  소셜 로그인 관련 URL 전체 허용
                        .requestMatchers("/oauth/**", "/social/**").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()


                        // 네이버 검색 api
                        .requestMatchers("/api/naver/**").permitAll()

                        // 회원가입, 로그인
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()

                        // 게시판 GET은 누구나, 나머지는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/qnaboards/**","/api/community/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/qnaboards/**","/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/qnaboards/**","/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/qnaboards/**","/api/community/**").authenticated()

                        // 관리자 전용
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")

                        //  정적 이미지 허용 (이미지 접근 허용)
                        .requestMatchers("/uploads/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/admin/products/**").permitAll()
                                .requestMatchers(
                                        "/", "/admin/**",
                                        "/mypage/**", "/community/**",
                                        "/popular/**", "/product/**"
                                ).permitAll()



                                // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )


                //  JWT 필터 등록 (UsernamePasswordAuthenticationFilter 앞에 추가)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
