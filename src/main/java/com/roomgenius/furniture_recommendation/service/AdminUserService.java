package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.UserDTO;

import java.util.List;

public interface AdminUserService {

    List<UserDTO> getUsers(String keyword, String role, String sort);

    void updateUserRole(Integer id, String role);
}
