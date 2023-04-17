package com.amd.backend.Global.Result.Exception;

import com.amd.backend.Global.Result.Error.ErrorCode;

public class UserAlreadyLogoutException extends BusinessException {
    public UserAlreadyLogoutException() {
        super(ErrorCode.USER_ALREADY_LOGOUT);
    }
}
