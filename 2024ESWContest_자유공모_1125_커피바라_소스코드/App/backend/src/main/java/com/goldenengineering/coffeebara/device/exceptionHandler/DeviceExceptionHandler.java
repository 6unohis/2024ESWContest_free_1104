package com.goldenengineering.coffeebara.device.exceptionHandler;

import com.goldenengineering.coffeebara.common.response.BaseExceptionResponse;
import com.goldenengineering.coffeebara.device.exception.DeviceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DeviceExceptionHandler {

    @ExceptionHandler(DeviceException.class)
    public BaseExceptionResponse handle_DeviceException(DeviceException e) {
        log.info("DeviceExceptionHandler deviceException : {}", e.getMessage());

        return new BaseExceptionResponse<>(e.getResponseStatus());
    }
}
