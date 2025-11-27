package com.korit.security2_study.mapper;

import com.korit.security2_study.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByUserId(Integer userId);
    void addUser(User user);

}
