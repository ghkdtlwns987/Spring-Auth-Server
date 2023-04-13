package com.amd.backend.Domain.User.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * userId를 가지고 사용자를 조회할 떄 사옹되는 DTO입니다.
 * @author : 황시준
 * @since : 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserDTO {
    private String email;
    private String name;
    private String userId;

    private List<ResponseUserDTO> data;
}
