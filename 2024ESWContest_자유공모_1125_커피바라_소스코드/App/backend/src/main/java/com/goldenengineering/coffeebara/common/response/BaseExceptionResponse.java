package com.goldenengineering.coffeebara.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.goldenengineering.coffeebara.common.response.status.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonPropertyOrder({"code", "message"})
public class BaseExceptionResponse <T> implements ResponseStatus {

    private final int code;

    public BaseExceptionResponse(ResponseStatus responseStatus) {
        this.code = responseStatus.getCode();
        this.message = responseStatus.getMessage();
    }

    @Setter
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
