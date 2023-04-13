package com.amd.backend.Global.Config.JWT.Token;

/**
 * Token값을 return 할 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
