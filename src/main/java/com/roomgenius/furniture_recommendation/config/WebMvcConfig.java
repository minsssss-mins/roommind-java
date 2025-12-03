package com.roomgenius.furniture_recommendation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 로컬: uploadDir = "uploads"
        // EC2: uploadDir = "/home/ubuntu/uploads"
        String resolvedPath = uploadDir;

        // 로컬 기본값일 경우: 상대경로 uploads → 절대경로로 변환
        if (!resolvedPath.startsWith("/")) {
            resolvedPath = System.getProperty("user.dir") + "/" + resolvedPath;
        }

        // 마지막 슬래시 보장
        if (!resolvedPath.endsWith("/")) {
            resolvedPath += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + resolvedPath);

        System.out.println("🔗 Static Resource Mapping: /uploads/** → " + resolvedPath);
    }
}
