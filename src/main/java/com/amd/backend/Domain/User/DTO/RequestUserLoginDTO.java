package com.amd.backend.Domain.User.DTO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 로그인 요청시 데이터를 받을 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Data
public class RequestUserLoginDTO {
    @NotBlank(message = "Email cannot be Null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equals or greather than 8 characters")
    private String pwd;
}
