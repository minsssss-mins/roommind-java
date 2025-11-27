package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminUserMapper {

    List<UserDTO> getUsers(
            @Param("keyword") String keyword,
            @Param("role") String role,
            @Param("sort") String sort
    );


    void updateUserRole(@Param("id") Integer id,
                        @Param("role") String role);
}
