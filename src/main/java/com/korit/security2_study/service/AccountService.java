package com.korit.security2_study.service;

import com.korit.security2_study.dto.ApiRespDto;
import com.korit.security2_study.dto.ModifyPasswordReqDto;
import com.korit.security2_study.entity.User;
import com.korit.security2_study.repository.UserRepository;
import com.korit.security2_study.security.model.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        if (!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근이다", null);
        }
        Optional<User> foundUser = userRepository.getUserByUserId(modifyPasswordReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않은 사용자 입니다", null);
        }
        if (!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "기존 비밀번호가 일치 하지 않습니다", null);
        }
        int result = userRepository.updatePassword(modifyPasswordReqDto.toEntity(bCryptPasswordEncoder));
        if(result !=1){
            return new ApiRespDto<>("failed" ,"문제가 발생했습니다 나중에 다시 입력하세요", null);
        }
        return new ApiRespDto<>("success","비밀번호가 변경 완료 됐습니다",null);
    }
}
