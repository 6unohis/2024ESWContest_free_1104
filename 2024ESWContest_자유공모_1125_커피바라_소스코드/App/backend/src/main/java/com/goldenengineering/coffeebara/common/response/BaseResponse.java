package com.goldenengineering.coffeebara.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.goldenengineering.coffeebara.common.response.status.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.SUCCESS;

@Getter
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse <T> implements ResponseStatus {

    private final int code;

    @Setter
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse() {
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
    }

    public BaseResponse(T result) {
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
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
