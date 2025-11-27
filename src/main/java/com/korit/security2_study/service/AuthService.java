package com.korit.security2_study.service;

import com.korit.security2_study.dto.ApiRespDto;
import com.korit.security2_study.dto.SigninReqDto;
import com.korit.security2_study.dto.SignupReqDto;
import com.korit.security2_study.entity.User;
import com.korit.security2_study.entity.UserRole;
import com.korit.security2_study.repository.UserRepository;
import com.korit.security2_study.repository.UserRoleRepository;
import com.korit.security2_study.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> signup(SignupReqDto signupReqDto){
        //username 중복검사
        Optional<User> foundUser = userRepository.getUserByUsername(signupReqDto.getUsername());
        if (foundUser.isPresent()){
            return new ApiRespDto<>("failed", "이미 존제 한다 아이디가", null);
        }
        //추가
        Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
        //role 추가
        if(optionalUser.isEmpty()){
            return new ApiRespDto<>("failed", "오류", null);
        }

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);
        return new ApiRespDto<>("success","회원가입이 완료 됐다", optionalUser.get());
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto){
        //username을 가지 ㄴ정보가 있는지 조회
        Optional<User> foundUser = userRepository.getUserByUsername(signinReqDto.getUsername());
        if (foundUser.isEmpty()){
            return new ApiRespDto<>("failed", "사용자 정보 다시 확인 부탁", null);
        }
        User user = foundUser.get();
        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())){
            return new ApiRespDto<>("failed", "사용자 정보 다시 확인 부탁", null);
        }

        String token = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인 성공", token);

    }
    public ApiRespDto<?> getUserByUsername(String username){
        Optional<User>foundUser = userRepository.getUserByUsername(username);
        if (foundUser.isEmpty()){
            return new ApiRespDto<>("failed", "존재하지 않는 회원이다", null);

        }
        return new ApiRespDto<>("success", "회원 조회 완료", foundUser.get());
    }
}
