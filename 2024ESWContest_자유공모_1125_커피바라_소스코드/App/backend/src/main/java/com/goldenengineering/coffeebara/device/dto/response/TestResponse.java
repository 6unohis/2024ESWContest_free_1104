package com.goldenengineering.coffeebara.device.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TestResponse (
        Integer stage,
        List<CupInfo> cup_infos
){
    @Builder
    public record CupInfo(
            String size,
            Boolean holder,
            Boolean cap
    ){
    }
}
