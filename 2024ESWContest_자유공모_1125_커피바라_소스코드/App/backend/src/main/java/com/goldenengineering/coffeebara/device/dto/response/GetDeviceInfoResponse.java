package com.goldenengineering.coffeebara.device.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetDeviceInfoResponse (
        List<GetDeviceOnMapResponse.DeviceInfo> deviceInfos
){
}
