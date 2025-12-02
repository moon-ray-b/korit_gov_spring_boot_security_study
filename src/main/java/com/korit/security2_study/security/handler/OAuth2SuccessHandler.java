package com.korit.security2_study.security.handler;

import com.korit.security2_study.entity.OAuth2User;
import com.korit.security2_study.entity.User;
import com.korit.security2_study.mapper.OAuth2UserMapper;
import com.korit.security2_study.repository.OAuth2UserRepository;
import com.korit.security2_study.repository.UserRepository;
import com.korit.security2_study.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User 정보를 파싱
        DefaultOAuth2User defaultOAuth2User =(DefaultOAuth2User) authentication.getPrincipal();
        String provider = defaultOAuth2User.getAttribute("provider"); // 제공자 플랫폼을 들고오는것
        String providerUserId = defaultOAuth2User.getAttribute("providerUserId"); // 제공자 식별 ID를 들고 오는곳
        String email = defaultOAuth2User.getAttribute("email"); // 이메일도 식별함
        System.out.println("--------------------------------");
        System.out.println(provider);
        System.out.println(providerUserId);
        System.out.println(email);
        System.out.println("--------------------------------");

        //provider, providerUserId
        Optional<OAuth2User>foundOAuth2User = oAuth2UserRepository.getOAuth2UserByProviderAndProviderUserId(provider,providerUserId);
            //OAuth2 로그인을 통해 회원가입이 되어있지 않거나 아직 가입이 안된 상태
            if(foundOAuth2User.isEmpty()){
                response.sendRedirect("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId=" +providerUserId + "&email=" + email );
                return;
            }
            //연동된 사용자가 있다면? => userId를 통해 회원 정보를 조회
            Optional<User> foundUser = userRepository.getUserByUserId(foundOAuth2User.get().getUserId());
            String accessToken = null;
            if (foundUser.isPresent()){
                accessToken = jwtUtils.generateAccessToken(Integer.toString(foundOAuth2User.get().getUserId()));
            }
            response.sendRedirect("http://localhost:3000/auth/oauth2/signin?accessToken=" + accessToken);

    }
}
