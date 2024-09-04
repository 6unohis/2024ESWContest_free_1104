package com.goldenengineering.coffeebara.common.response.status;

public interface ResponseStatus {
    int getCode();
    String getMessage();
    void setMessage(String message);
}
