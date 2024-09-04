package com.goldenengineering.coffeebara.common.response.status;

import lombok.Setter;

public enum BaseExceptionResponseStatus implements ResponseStatus{

    SUCCESS(200, "성공적으로 완료되었습니다."),

    INVALID_USER_FIELD(500),
    INVALID_DEVICE_INFO(600),
    NO_USER_EXIST(501, "해당하는 유저가 없습니다.")
    ;

    private final Integer code;
    @Setter
    private String message;

    BaseExceptionResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    BaseExceptionResponseStatus(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
