package com.amd.backend.Domain.User.Controller;

import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Entity.UserEntity;
import com.amd.backend.Domain.User.Service.AuthService;
import com.amd.backend.Domain.User.Repository.UserService;
import com.amd.backend.Domain.User.VO.Greeting;
import com.amd.backend.Domain.User.DTO.RequestUserRegisterDTO;
import com.amd.backend.Domain.User.DTO.ResponseUserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
// @RequestMapping("/auth")
@RequestMapping("/") // API Gateway에서 해당 요청으로 매핑하도록 했기 때문에 @RequestMapping이 불필요함.
public class UserController {
    private final Environment env;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService, AuthService authService){
        this.env = env;
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Health Check에 쓰이는 API입니다
     * @return : server.port
     * @author : 황시준
     * @since : 1.0
     */
    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in User Service on PORT  %s",
                env.getProperty("local.server.port"));
    }

    /**
     * Welcome Page입니다.
     * @author : 황시준
     * @since : 1.0
     */
    @GetMapping("/welcome")
    public String wwelcome(){
        //return env.getProperty("greeting.message");     // application.yml 파일에 있는 greeting.message를 가져옴.
        return greeting.getMessage();
    }

    /**
     * 전체 회원 정보를 조회하는 API입니다.
     * @return 전체 회원 목록
     * @author : 황시준
     * @since : 1.0
     */
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserDTO>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUserDTO> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUserDTO.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 회원 정보를 검색하는 API입니다.
     * 검색할 때는 email을 사용합니다.
     * @param email
     * @return ResponseEntity
     * @author : 황시준
     * @since : 1.0
     */
    @GetMapping("/{email}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable("email") String email){
        UserDTO userDTO = userService.getUserDetailsByEmail(email);

        ResponseUserDTO returnValue = new ModelMapper().map(userDTO, ResponseUserDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}