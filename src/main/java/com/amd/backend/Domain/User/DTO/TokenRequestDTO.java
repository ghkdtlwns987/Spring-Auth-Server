package com.amd.backend.Domain.User.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Reissue에 사용할 DTO입니다.
 */
@Getter
@NoArgsConstructor
public class TokenRequestDTO {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
