package com.goldenengineering.coffeebara.user.exceptionHandler;

import com.goldenengineering.coffeebara.common.response.BaseExceptionResponse;
import com.goldenengineering.coffeebara.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    public BaseExceptionResponse handle_UserException(UserException e) {
        log.info("UserExceptionHandler userException : {}", e.getMessage());

        return new BaseExceptionResponse<>(e.getResponseStatus());
    }
}
