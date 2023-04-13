package com.amd.backend.Domain.User.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 전체 회원 정보를 담는 DTO 입니다.
 * ModelMapper()로 DTO값을 변환합니다.
 * @author : 황시준
 * @since : 1.0
 */
@Data
public class UserDTO {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createAt;

    private String encryptedPwd;

    private List<ResponseParseDataDTO> data;
}
