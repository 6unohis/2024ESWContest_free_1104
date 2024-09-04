package com.goldenengineering.coffeebara.device;

import com.goldenengineering.coffeebara.common.response.BaseResponse;
import com.goldenengineering.coffeebara.device.dto.request.SaveCapacityRequest;
import com.goldenengineering.coffeebara.device.dto.response.GetDeviceInfoResponse;
import com.goldenengineering.coffeebara.device.dto.response.GetDeviceOnMapResponse;
import com.goldenengineering.coffeebara.device.dto.response.TestResponse;
import com.goldenengineering.coffeebara.device.exception.DeviceException;
import com.goldenengineering.coffeebara.device.validation.DeviceValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.INVALID_DEVICE_INFO;

@Slf4j
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceValidation deviceValidation;

    @PatchMapping("")
    public BaseResponse saveCapacity(@Validated @RequestBody SaveCapacityRequest saveCapacityRequest, BindingResult bindingResult) {
        log.info("DeviceController.saveCapacity");

        if(bindingResult.hasErrors()) {
            throw new DeviceException(INVALID_DEVICE_INFO, bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }

        deviceValidation.isExistDeviceWithId(saveCapacityRequest.deviceId());

        deviceService.saveCapacity(saveCapacityRequest);

        return new BaseResponse<>();
    }

    @GetMapping("/map")
    public BaseResponse<GetDeviceOnMapResponse> getDeviceOnMap(
            @RequestParam Double southLeftLatitude,
            @RequestParam Double southLeftLongitude,
            @RequestParam Double northRightLatitude,
            @RequestParam Double northRightLongitude
    ) {
        log.info("DeviceController.getDeviceOnMap");

        deviceValidation.isValidLatitude(southLeftLatitude);
        deviceValidation.isValidLatitude(northRightLatitude);
        deviceValidation.isValidLongitude(southLeftLongitude);
        deviceValidation.isValidLongitude(northRightLongitude);

        return new BaseResponse<>(deviceService.getDeviceOnMap(southLeftLatitude,southLeftLongitude,northRightLatitude,northRightLongitude));
    }

    @GetMapping("")
    public BaseResponse<GetDeviceInfoResponse> getDeviceInfo() {
        log.info("DeviceController.getDeviceOnMap");

        return new BaseResponse<>(deviceService.getDeviceInfo());
    }

    @GetMapping("/test")
    public BaseResponse<TestResponse> test() {
        log.info("DeviceController.test");

        List<TestResponse.CupInfo> cupInfos = List.of(
                TestResponse.CupInfo.builder()
                        .size("Venti")
                        .cap(true)
                        .holder(true)
                        .build(),
                TestResponse.CupInfo.builder()
                        .size("Grande")
                        .cap(true)
                        .holder(false)
                        .build()
        );

        return new BaseResponse<>(
                TestResponse.builder()
                        .stage(1)
                        .cup_infos(cupInfos)
                        .build()
        );
    }
}
