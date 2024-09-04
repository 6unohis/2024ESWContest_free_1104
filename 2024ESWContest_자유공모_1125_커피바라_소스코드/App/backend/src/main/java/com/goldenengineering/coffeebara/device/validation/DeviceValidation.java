package com.goldenengineering.coffeebara.device.validation;

import com.goldenengineering.coffeebara.device.exception.DeviceException;
import com.goldenengineering.coffeebara.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.goldenengineering.coffeebara.common.response.status.BaseExceptionResponseStatus.INVALID_DEVICE_INFO;

@Component
@RequiredArgsConstructor
public class DeviceValidation {

    private final DeviceRepository deviceRepository;

    public void isExistDeviceWithId(Long id){
        if(!deviceRepository.existsById(id)){
            throw new DeviceException(INVALID_DEVICE_INFO, "일치하는 디바이스가 없습니다.");
        }
    }

    public void isValidLatitude(Double southLeftLatitude) {
        if(southLeftLatitude < 33){
            throw new DeviceException(INVALID_DEVICE_INFO, "위도는 33보다 작을 수 없습니다.");
        }
        if(southLeftLatitude > 39){
            throw new DeviceException(INVALID_DEVICE_INFO, "위도는 39보다 클 수 없습니다.");
        }
    }

    public void isValidLongitude(Double southLeftLongitude) {
        if(southLeftLongitude < 125){
            throw new DeviceException(INVALID_DEVICE_INFO, "경도는 125보다 작을 수 없습니다.");
        }
        if(southLeftLongitude > 130){
            throw new DeviceException(INVALID_DEVICE_INFO, "경도는 130보다 클 수 없습니다.");
        }
    }
}
