package com.korit.security2_study.dto;

import com.korit.security2_study.entity.OAuth2User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class OAuth2MergeReqDto {
    private String username;
    private String password;
    private String provider;
    private String providerUserId;

    public OAuth2User toEntity(Integer userId){
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }

}
