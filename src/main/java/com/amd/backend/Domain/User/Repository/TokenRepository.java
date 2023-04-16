package com.amd.backend.Domain.User.Repository;

import com.amd.backend.Domain.User.DTO.TokenDTO;
import com.amd.backend.Domain.User.DTO.UserDTO;

import java.util.Date;

public interface TokenRepository {
    String createAccessToken(String userId);
    String createRefreshToken(String userId);
    String expireToken(String userId);
    Date extractExpiredTime(String accessToken);
}
