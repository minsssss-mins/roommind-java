package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.User;

public interface UserService {
    int register(User user);
    User findById(int userId);
}
