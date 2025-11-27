package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.UserDTO;
import com.roomgenius.furniture_recommendation.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    @Override
    public List<UserDTO> getUsers(String keyword, String role, String sort) {
        return adminUserMapper.getUsers(keyword, role, sort);
    }

    @Override
    public void updateUserRole(Integer id, String role) {
        adminUserMapper.updateUserRole(id, role);
    }
}
