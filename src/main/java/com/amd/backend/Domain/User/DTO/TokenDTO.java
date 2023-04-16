package com.amd.backend.Domain.User.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Response Token값을 전달해줄 DTO입니다.
 */

@Getter
@Setter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;    // 추후 작업
    private String uuid;
    private Long expiredTime;
}
