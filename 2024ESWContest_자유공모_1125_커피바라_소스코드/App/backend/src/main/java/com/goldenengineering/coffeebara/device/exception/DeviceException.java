package com.goldenengineering.coffeebara.device.exception;

import com.goldenengineering.coffeebara.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class DeviceException extends RuntimeException{
    private final ResponseStatus responseStatus;

    public DeviceException(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public DeviceException(ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        responseStatus.setMessage(message);
    }
}
