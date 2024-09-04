package com.goldenengineering.coffeebara.user;

import com.goldenengineering.coffeebara.common.enums.Role;
import com.goldenengineering.coffeebara.user.dto.request.CreateUserRequest;
import com.goldenengineering.coffeebara.user.dto.request.LoginUserRequest;
import com.goldenengineering.coffeebara.user.dto.response.CreateUserResponse;
import com.goldenengineering.coffeebara.user.exception.UserException;
import com.goldenengineering.coffeebara.user.model.UserJpaEntity;
import com.goldenengineering.coffeebara.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.NO_USER_EXIST;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("UserService createUser");

        UserJpaEntity user = UserJpaEntity.builder()
                .identifier(createUserRequest.id())
                .password(createUserRequest.password())
                .userName(createUserRequest.name())
                .latitude(createUserRequest.latitude())
                .longitude(createUserRequest.longitude())
                .role(Role.MANAGER)
                .build();

        Long userId = userRepository.save(user).getUserId();

        return CreateUserResponse.builder()
                .userId(userId)
                .build();
    }

    public CreateUserResponse loginUser(LoginUserRequest loginUserRequest) {
        log.info("UserService loginUser");

        Optional<UserJpaEntity> userJpaEntity = userRepository.findByIdentifierAndPassword(
                loginUserRequest.id(),
                loginUserRequest.password()
        );

        if(userJpaEntity.isEmpty()) {
            throw new UserException(NO_USER_EXIST);
        }

        return new CreateUserResponse(userJpaEntity.get().getUserId());
    }
}
