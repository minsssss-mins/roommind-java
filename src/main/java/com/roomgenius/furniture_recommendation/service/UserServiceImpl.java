package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.User;
import com.roomgenius.furniture_recommendation.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public int register(User user) {
        if (user.getRole() == null) {
            user.setRole("user");
        }
        return userMapper.insert(user);
    }

    @Override
    public User findById(int userId) {
        return userMapper.findById(userId);
    }
}
