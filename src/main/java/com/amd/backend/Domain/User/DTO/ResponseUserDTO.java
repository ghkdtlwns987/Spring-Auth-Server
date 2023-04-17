package com.amd.backend.Domain.User.DTO;

import com.amd.backend.Domain.User.Entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * userId를 가지고 사용자를 조회할 떄 사옹되는 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserDTO {
    private String userId;
    private String email;
    private String name;

    // private List<ResponseUserDTO> data;

    public static ResponseUserDTO of(UserEntity userEntity){
        return new ResponseUserDTO(userEntity.getUserId(), userEntity.getEmail(), userEntity.getName());
    }
}
