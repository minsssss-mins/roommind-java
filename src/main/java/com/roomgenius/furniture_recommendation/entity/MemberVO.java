package com.roomgenius.furniture_recommendation.entity;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    private String email;
    private String password;
    private String username;
    private String regAt;


}
