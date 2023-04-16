package com.amd.backend.Domain.User.Controller;

import com.amd.backend.Domain.User.DTO.*;
import com.amd.backend.Domain.User.Repository.UserService;
import com.amd.backend.Domain.User.Service.AuthService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 인증관련 서비스 Controller입니다.
 * @author : 황시준
 * @since : 1.0
 */
@RestController
@RequestMapping("/") // API Gateway에서 해당 요청으로 매핑하도록 했기 때문에 @RequestMapping이 불필요함.
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    /**
     * 회원가입 시 처리되는 API입니다.
     * @param user
     * @return ResponseUser
     * @author : 황시준
     * @since : 1.0
     */
    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDTO> signup(@RequestBody RequestUserRegisterDTO user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDTO userDTO = mapper.map(user, UserDTO.class);
        authService.signup(userDTO);

        //ResponseUser reponseUser = mapper.map(user,UserDTO.class);
        ResponseUserDTO responseUserDTO = mapper.map(userDTO, ResponseUserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDTO);

    }

    /**
     * 로그아웃 처리를 담당하는 메소드입니다.
     * @param userId
     * @param token
     * @return
     * @author : 황시준
     * @since : 1.0
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name= "UUID") String userId, @RequestHeader(name = "Authorization") String token){

        if (isValidHeader(token, userId)) {
            System.out.println("유효하지 않은 헤더입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout Error");
        }
        else{
            authService.doLogout(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Logout Success");
    }
    /**
     * Logout 할 때 Authentication 헤더가 들어있는지 확인하는 함수입니다.
     * @param token
     * @return
     */
    private boolean isValidHeader(String token, String userId) {
        if(Objects.isNull(token) || Objects.isNull(userId)){
            return false;
        }
        return true;
    }
}
