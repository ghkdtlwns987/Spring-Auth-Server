package com.amd.backend.Global.Result.Exception;

import com.amd.backend.Global.Result.Error.ErrorCode;

public class RefreshTokenInvalidException extends BusinessException{
    public RefreshTokenInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_INVALID);
    }
}
