package com.goldenengineering.coffeebara.user.dto.request;

import jakarta.validation.constraints.*;

public record CreateUserRequest (

        @NotNull(message = "유저 아이디는 필수입니다.")
        @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
        String id,

        @NotNull(message = "유저 비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
        String password,

        @NotNull(message = "유저 이름은 필수입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
        String name,

        @Min(value = 33, message = "위도는 33보다 작을 수 없습니다." )
        @Max(value = 39, message = "위도는 39보다 클 수 없습니다.")
        Double latitude,

        @Min(value = 125, message = "경도는 125보다 작을 수 없습니다." )
        @Max(value = 130, message = "경도는 130보다 클 수 없습니다.")
        Double longitude
){
}
