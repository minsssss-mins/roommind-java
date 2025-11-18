package com.roomgenius.furniture_recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverShoppingService {

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map searchProduct(String query) {

        String url = "https://openapi.naver.com/v1/search/shop.json?query=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        return response.getBody();
    }
}

