package com.roomgenius.furniture_recommendation.repository;

import com.roomgenius.furniture_recommendation.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM Users WHERE userId = #{userId}")
    User findById(int userId);

    @Select("SELECT * FROM Users")
    List<User> findAll();

    @Insert("INSERT INTO Users (username, phone, address, email, password, role, createdDate, updatedDate) " +
            "VALUES (#{username}, #{phone}, #{address}, #{email}, #{password}, #{role}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE Users SET username=#{username}, phone=#{phone}, address=#{address}, " +
            "email=#{email}, password=#{password}, role=#{role}, updatedDate=NOW() WHERE userId=#{userId}")
    int update(User user);

    @Delete("DELETE FROM Users WHERE userId=#{userId}")
    int delete(int userId);
}
