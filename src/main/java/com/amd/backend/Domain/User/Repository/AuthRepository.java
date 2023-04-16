package com.amd.backend.Domain.User.Repository;

import com.amd.backend.Domain.User.DTO.TokenDTO;
import com.amd.backend.Domain.User.DTO.UserDTO;

public interface AuthRepository {
    void doLogout(String userId);
}
