package com.goldenengineering.coffeebara.user;

import com.goldenengineering.coffeebara.common.response.BaseResponse;
import com.goldenengineering.coffeebara.user.dto.request.CreateUserRequest;
import com.goldenengineering.coffeebara.user.dto.request.LoginUserRequest;
import com.goldenengineering.coffeebara.user.dto.response.CreateUserResponse;
import com.goldenengineering.coffeebara.user.exception.UserException;
import com.goldenengineering.coffeebara.user.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.INVALID_USER_FIELD;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;

    @PostMapping("")
    public BaseResponse<CreateUserResponse> createUser(@Validated @RequestBody CreateUserRequest createUserRequest, BindingResult bindingResult){
        log.info("UserController createUser");

        if(bindingResult.hasErrors()){
            throw new UserException(INVALID_USER_FIELD, bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }

        userValidation.makeExceptionIfUserIdDuplicate(createUserRequest.id());

        return new BaseResponse<>(userService.createUser(createUserRequest));
    }

    @PostMapping("/login")
    public BaseResponse<CreateUserResponse> loginUser(@Validated @RequestBody LoginUserRequest loginUserRequest, BindingResult bindingResult){
        log.info("UserController loginUser");

        if(bindingResult.hasErrors()){
            throw new UserException(INVALID_USER_FIELD, bindingResult.getFieldErrors().get(0).getDefaultMessage() );
        }

        return new BaseResponse<>(userService.loginUser(loginUserRequest));
    }
}
