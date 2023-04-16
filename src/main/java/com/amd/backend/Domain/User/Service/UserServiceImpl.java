package com.amd.backend.Domain.User.Service;



import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Entity.UserEntity;
import com.amd.backend.Domain.User.Repository.UserRepository;
import com.amd.backend.Domain.User.DTO.ResponseParseDataDTO;
import com.amd.backend.Domain.User.Repository.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * UserID를 기반으로 회원을 검색하는 API입니다.
     * 회원을 검색할 때 fidnByUserId() 메소드로 검색합니다.
     * @param userId
     * @return userDTO
     * @author : 황시준
     * @since : 1.0
     */
    @Override
    public UserDTO getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Founded");
        }

        List<ResponseParseDataDTO> data = new ArrayList<>();

        UserDTO userDTO = new ModelMapper().map(userEntity, UserDTO.class);
        userDTO.setData(data);

        return userDTO;
    }

    /**
     * 전체 회원을 조회하는 API입니다.
     * 이 때 CRUD Repository에서 지원하는 findAll() 메소드를 사용합니다.
     * @return Iterable<UserEntity>
     * @author : 황시준
     * @since : 1.0
     */
    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if(userEntity == null){
            throw new UsernameNotFoundException(username);
        }

        // 검색이 잘 됬다면
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()); // 권한을 List형태로 반환
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
        return userDTO;
    }
}