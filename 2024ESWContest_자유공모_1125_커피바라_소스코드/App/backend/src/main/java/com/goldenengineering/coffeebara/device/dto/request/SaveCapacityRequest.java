package com.goldenengineering.coffeebara.device.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaveCapacityRequest(
        @NotNull(message = "디바이스 아이디는 필수입니다.")
        Long deviceId,

        @NotNull(message = "디바이스 용량은 필수입니다.")
        @Min(value = 0, message = "용량은 0 이하일 수 없습니다.")
        @Max(value = 100, message = "용량은 100 이상일 수 없습니다.")
        Integer capacity
) {
}
