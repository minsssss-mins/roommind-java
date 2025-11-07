package com.roomgenius.furniture_recommendation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html 렌더링
    }

    @GetMapping("/api/health")
    public String health() {
        return "서버 정상 작동 중 🚀";
    }
}
