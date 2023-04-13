package com.amd.backend.Global.Config.JWT.Token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Token을 요청할 때 사용되는 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
public class TokenRequestDTO {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}