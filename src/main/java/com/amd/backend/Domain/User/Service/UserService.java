package com.amd.backend.Domain.User.Service;

import com.amd.backend.Domain.User.DTO.UserDTO;
import com.amd.backend.Domain.User.Entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO signup(UserDTO userDTO);
    UserDTO getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();
    UserDTO getUserDetailsByEmail(String userName);
}
