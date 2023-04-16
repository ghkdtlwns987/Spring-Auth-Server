package com.amd.backend.Domain.User.Service;

import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Entity.UserEntity;
import com.amd.backend.Domain.User.Repository.UserRepository;
import com.amd.backend.Global.Config.JWT.Token.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 인증관련 서비스들을 저장한 클래스입니다.
 * @author : 황시준
 * @param : 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * 로그아웃을 담당하는 메소드입니다.
     * 로그아웃 시 Token Expire 시간을 만료시간으로 변경합니다.
     * @param userId
     * @author : 황시준
     * @since : 1.0
     */
    @Transactional
    public void doLogout(String userId) {
        tokenProvider.expireToken(userId);
    }

    /**
     * signup 요청이 들어올 시 실행되는 메소드입니다.
     * 비밀번호는 BCryptpasswordEncoder를 통해 데이터베이스에 암호화하여 저장했습니다.
     * userDTO객체의 요소를 userEntity로 변환할 때 ModelMapper를 사용합니다.
     * @param userDTO
     * @return UserDTO
     */
    @Transactional
    public UserDTO signup(UserDTO userDTO){
        userDTO.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDTO.getPwd()));
        userEntity.setCreateAt(new Date());

        userRepository.save(userEntity);

        UserDTO returnUserDTO = mapper.map(userEntity, UserDTO.class);
        return returnUserDTO;
    }
}
