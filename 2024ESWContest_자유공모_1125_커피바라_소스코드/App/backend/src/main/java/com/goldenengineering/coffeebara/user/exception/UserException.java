package com.goldenengineering.coffeebara.user.exception;

import com.goldenengineering.coffeebara.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException{
    private ResponseStatus responseStatus;

    public UserException(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public UserException(ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        responseStatus.setMessage(message);
    }
}
