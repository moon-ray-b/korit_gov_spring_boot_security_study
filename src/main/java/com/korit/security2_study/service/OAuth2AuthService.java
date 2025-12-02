package com.korit.security2_study.service;

import com.korit.security2_study.dto.ApiRespDto;
import com.korit.security2_study.dto.OAuth2SignupReqDto;
import com.korit.security2_study.entity.User;
import com.korit.security2_study.entity.UserRole;
import com.korit.security2_study.repository.OAuth2UserRepository;
import com.korit.security2_study.repository.UserRepository;
import com.korit.security2_study.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//oauth2로 회원가입 또는 연동 해주는 서비스 역할
@Service
public class OAuth2AuthService {

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> signup(OAuth2SignupReqDto oAuth2SignupReqDto){
        Optional<User> foundUser = userRepository.getUserByEmail(oAuth2SignupReqDto.getEmail());

        if (foundUser.isPresent()){
            return new ApiRespDto<>("failed", "이미 존재하는 이메일 입니다.",  null);
        }
        Optional<User>foundUserByUsername = userRepository.getUserByUsername(oAuth2SignupReqDto.getUsername());
        if (foundUserByUsername.isPresent()){
            return new ApiRespDto<>("failed", "이미 존재하는 사용자 이름입니다", null);
        }

        Optional<User> optionalUser= userRepository.addUser(oAuth2SignupReqDto.toUserEntity(bCryptPasswordEncoder));
        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);
        oAuth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toOAuth2UserEntity(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success", oAuth2SignupReqDto.getProvider()+" 회원가입 완료", null);
    }
}
