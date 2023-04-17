package com.amd.backend.Global.Result.Exception;

import com.amd.backend.Global.Result.Error.ErrorCode;

public class NoAuthorityException extends BusinessException {
    public NoAuthorityException() {
        super(ErrorCode.NO_AUTHORITY);
    }
}