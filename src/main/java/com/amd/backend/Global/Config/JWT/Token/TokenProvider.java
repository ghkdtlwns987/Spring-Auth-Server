package com.amd.backend.Global.Config.JWT.Token;

import com.amd.backend.Domain.User.Repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 토큰을 제공하는 클래스입니다.
 * @author : 황시준
 * @since : 1.0
 */

@Component
@RequiredArgsConstructor
public class TokenProvider implements TokenRepository {

    @Value("${token.secret}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30 minutes
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    /** Token을 생성하는 메소드입니다.
     *
     * @param userId
     * @param ExpiredTime
     * @return String
     * @author : 황시준
     * @since : 1.0
     */
    public String createToken(String userId, long ExpiredTime){
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ExpiredTime);

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }

    /**
     * Token을 발급하고 반환하는 메소드입니다.
     * @author : 황시준
     * @since : 1.0
     */
    public String createAccessToken(String userId){
        return createToken(userId, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * Refresh Token을 발급하는 메소드입니다.
     * @param userId
     * @return
     * @author : 황시준
     * @since : 1.0
     */
    public String createRefreshToken(String userId){
        return createToken(userId, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /** Token을 만료시키는 메소드입니다.
     *  AMD서비스에서는 토큰 만료시간을 현재시간으로 만들어 로그아웃을 처리합니다.
     * @return String
     * @author : 황시준
     * @since : 1.0
     */
    public String expireToken(String userId){
        long now = (new Date()).getTime();
        return createToken(userId, now);
    }

    /**
     * accessToken에서 토큰 만료시간을 추출하는 메소드입니다.
     * @param accessToken
     * @return Date
     * @author : 황시준
     * @since : 1.0
     */
    public Date extractExpiredTime(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
    }
}
