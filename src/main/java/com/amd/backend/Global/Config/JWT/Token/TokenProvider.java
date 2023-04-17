package com.amd.backend.Global.Config.JWT.Token;

import com.amd.backend.Domain.User.Repository.TokenRepository;
import com.amd.backend.Global.Result.Exception.NoAuthorityException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 토큰을 제공하는 클래스입니다.
 * @author : 황시준
 * @since : 1.0
 */

@Slf4j
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

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);
        // Claims에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("Authentication").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어 Authentication return
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
