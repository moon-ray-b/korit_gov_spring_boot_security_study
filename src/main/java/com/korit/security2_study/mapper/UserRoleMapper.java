package com.korit.security2_study.mapper;

import com.korit.security2_study.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    void addUserRole(UserRole userRole);
}
