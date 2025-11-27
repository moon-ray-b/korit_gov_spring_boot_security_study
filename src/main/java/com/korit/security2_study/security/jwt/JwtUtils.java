package com.korit.security2_study.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key Key;

    public JwtUtils(@Value("${jwt.secret}") String secret){
        Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String id){
        return Jwts.builder()
                .subject("AccessToken")
                .id(id)
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L *30L)))
                .signWith(Key)
                .compact();
    }
    //Claims: JWT의 Payload 영여그 즉 사용자 정보 , 만료일자 등등
    //JwtException: 토큰이 잘못되어있을 경우(위변조, 만료 등) 발생하는 예외
    public Claims getclaims(String token)throws JwtException {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();

        //파싱을 할려면 기존에 가지고 있는 비밀키가 필요함.(비밀키가 꼭 필요)
        jwtParserBuilder.setSigningKey(Key);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody(); //순수 Claims JWT를 파싱
    }

    public boolean isBearer(String token){
        if (token == null){
            return false;
        }
        if (!token.startsWith("Bearer ")){
            return false;
        }
        return true;
    }
    public String removeBearer(String bearerToken){
        return bearerToken.replaceFirst("Bearer ", "");

    }

}
