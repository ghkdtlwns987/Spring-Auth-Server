package com.amd.backend.Domain.User.Controller;

import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Entity.UserEntity;
import com.amd.backend.Domain.User.Service.UserService;
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

@RestController
// @RequestMapping("/auth")
@RequestMapping("/") // API Gateway에서 해당 요청으로 매핑하도록 했기 때문에 @RequestMapping이 불필요함.
public class UserController {
    private Environment env;
    private UserService userService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService){
        this.env = env;
        this.userService = userService;
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
        userService.signup(userDTO);

        //ResponseUser reponseUser = mapper.map(user,UserDTO.class);
        ResponseUserDTO responseUserDTO = mapper.map(userDTO, ResponseUserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDTO);
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
     * 검색할 때는 userId를 사용합니다.
     * @param userId
     * @return ResponseEntity
     * @author : 황시준
     * @since : 1.0
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable("userId") String userId){
        UserDTO userDTO = userService.getUserByUserId(userId);

        ResponseUserDTO returnValue = new ModelMapper().map(userDTO, ResponseUserDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}