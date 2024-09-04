package com.goldenengineering.coffeebara.device.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetDeviceOnMapResponse (
    List<DeviceInfo> devices
){
    @Builder
    public record DeviceInfo(
            Long device_id,
            String location,
            Double latitude,
            Double longitude,
            Integer capacity
    ){

    }
}
