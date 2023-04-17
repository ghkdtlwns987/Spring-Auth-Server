package com.amd.backend.Domain.User.DTO;

import com.amd.backend.Domain.User.Entity.UserEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

/**
 * 회원가입에 쓰이는 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Data
public class RequestUserRegisterDTO {
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name not be less than two characters")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    private String pwd;


    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(getEmail())
                .name(getName())
                .userId(UUID.randomUUID().toString())
                .encryptedPwd(getPwd())
                .createAt(new Date())
                .build();
    }


}