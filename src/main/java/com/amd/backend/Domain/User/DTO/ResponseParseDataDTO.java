package com.amd.backend.Domain.User.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * Backend-Parse 서비스에서 값을 받아와 데이터를 반환할 때 사용하는 DTO입니다.
 * 현재는 인공지능팀이 Database에 값을 어떤값을 넣을지 정해지지 않아 임시로 작성했습니다.
 * @author : 황시준
 * @since : 1.0
 */
@Getter
@Setter
public class ResponseParseDataDTO {
    private Integer idx;
    private Date createAt;
    private String data_key;
    private String data_value;

    private String dataId;
}
