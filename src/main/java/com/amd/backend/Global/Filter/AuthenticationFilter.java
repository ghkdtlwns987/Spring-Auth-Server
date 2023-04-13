package com.amd.backend.Global.Filter;

import com.amd.backend.Domain.User.DTO.RequestUserLoginDTO;
import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Login 기능을 위한 Authentication Filter를 구현하합니다.
 * @author : 황시준
 * @since : 1.0
 */
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30 minutes
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days
    private UserService userService;
    private Environment env;        // expiredToken, JWT_Secret

    public AuthenticationFilter(AuthenticationManager authenticationManager
                                ,UserService userService
                                ,Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.env = env;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            // 사용자 요청 정보를 ObjectMapper()로 읽어들인다.
            // 이 때 getInputStream()으로 받은 이유는 Login요청할 때는 POST방식으로 요청해 Request 파라미터로 받을 수 없기 때문이다.
            RequestUserLoginDTO creds = new ObjectMapper().readValue(request.getInputStream(), RequestUserLoginDTO.class);


            // 입력한 email과 pwd를 가지고 Authentication Token으로 변환시켜 인증 처리를 진행함.
            // getAuthenticationManager는 인증을 처리하는 함수임. (ID, PW비교)
            // 3번째 인자는 권한을 의미함.
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getEmail(),
                        creds.getPwd(),
                        new ArrayList<>()
                )
            );

        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException{
        log.debug(((User)authResult.getPrincipal()).getUsername());

        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDTO userDetails = userService.getUserDetailsByEmail(userName);

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String token =
                Jwts.builder()
                                .setSubject(userDetails.getUserId())
                                        .setExpiration(accessTokenExpiresIn)
                                                        .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                        .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }

}
