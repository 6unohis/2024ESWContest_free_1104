package com.goldenengineering.coffeebara.user.dto.response;

import lombok.Builder;

@Builder
public record CreateUserResponse (
        Long userId
){
}
