package com.amd.backend.Domain.User.DTO;

import lombok.Builder;

import javax.persistence.Id;

public class RefreshToken {
    @Id
    private String tokenkey;

    private String tokenvalue;

    public RefreshToken updateValue(String token_value){
        this.tokenvalue = token_value;
        return this;
    }

    @Builder
    public RefreshToken(String token_key, String token_value){
        this.tokenkey = token_key;
        this.tokenvalue = token_value;
    }
}
