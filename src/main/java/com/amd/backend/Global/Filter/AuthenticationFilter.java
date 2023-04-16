package com.amd.backend.Global.Filter;

import com.amd.backend.Domain.User.DTO.RequestUserLoginDTO;
import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Repository.TokenRepository;
import com.amd.backend.Domain.User.Repository.UserService;
import com.amd.backend.Global.Config.JWT.Token.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Login 기능을 위한 Authentication Filter를 구현하합니다.
 * @author : 황시준
 * @since : 1.0
 */
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;        // expiredToken, JWT_Secret
    private TokenRepository tokenRepository;
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String UUID_HEADER = "UUID";
    private final String EXPIRED_HEADER = "X-Expire";
    private static final String BEARER_PREFIX = "Bearer ";

    public AuthenticationFilter(AuthenticationManager authenticationManager
                                ,UserService userService
                                ,Environment env
                                ,TokenRepository tokenRepository) {
        super.setAuthenticationManager(authenticationManager);
        this.env = env;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
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

    /**
     * 인증이 완료되면 토큰을 발급합니다.
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     * @author : 황시준
     * @since : 1.0
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException{
        log.debug(((User)authResult.getPrincipal()).getUsername());

        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDTO userDetails = userService.getUserDetailsByEmail(userName);

        String accessToken = tokenRepository.createAccessToken(userDetails.getUserId());        // Access Token 생성
        Long expiredTime = tokenRepository.extractExpiredTime(accessToken).getTime();           // 토큰 만료시간

        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        response.addHeader(EXPIRED_HEADER, String.valueOf(expiredTime));
        response.addHeader(UUID_HEADER, userDetails.getUserId());
    }
}
