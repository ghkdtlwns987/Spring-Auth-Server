package com.amd.backend.Global.Result.Exception;


import com.amd.backend.Global.Result.Error.ErrorCode;

public class UserEmailAlreadyExistsException extends BusinessException {
    public UserEmailAlreadyExistsException(){
        super(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
