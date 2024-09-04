package com.goldenengineering.coffeebara.user.validation;

import com.goldenengineering.coffeebara.user.exception.UserException;
import com.goldenengineering.coffeebara.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.INVALID_USER_FIELD;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserRepository userRepository;

    public void makeExceptionIfUserIdDuplicate(String identifier) {
        if(userRepository.existsByIdentifier(identifier)){
            throw new UserException(INVALID_USER_FIELD, "중복된 아이디입니다.");
        }
    }
}
