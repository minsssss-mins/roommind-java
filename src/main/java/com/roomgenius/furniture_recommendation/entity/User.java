package com.roomgenius.furniture_recommendation.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String phone;
    private String address;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
