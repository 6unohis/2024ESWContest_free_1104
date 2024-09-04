package com.goldenengineering.coffeebara.device;

import com.goldenengineering.coffeebara.device.dto.request.SaveCapacityRequest;
import com.goldenengineering.coffeebara.device.dto.response.GetDeviceInfoResponse;
import com.goldenengineering.coffeebara.device.dto.response.GetDeviceOnMapResponse;
import com.goldenengineering.coffeebara.device.model.DeviceJpaEntity;
import com.goldenengineering.coffeebara.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public void saveCapacity(SaveCapacityRequest request) {
        log.info("DeviceService.saveCapacity");

        DeviceJpaEntity deviceJpaEntity = deviceRepository.findByDeviceId(request.deviceId());
        deviceJpaEntity.setCapacity(request.capacity());
        deviceRepository.save(deviceJpaEntity);
    }

    public GetDeviceOnMapResponse getDeviceOnMap(Double southLeftLatitude, Double southLeftLongitude, Double northRightLatitude, Double northRightLongitude) {
        log.info("DeviceService.getDeviceOnMap");

        List<DeviceJpaEntity> deviceJpaEntityList = deviceRepository.findByLatitudeBetweenAndLongitudeBetween(southLeftLatitude,northRightLatitude,southLeftLongitude,northRightLongitude);

        return GetDeviceOnMapResponse.builder()
                .devices(
                        deviceJpaEntityList.stream()
                                .map(a -> GetDeviceOnMapResponse.DeviceInfo.builder()
                                        .device_id(a.getDeviceId())
                                        .location(a.getLocation())
                                        .latitude(a.getLatitude())
                                        .longitude(a.getLongitude())
                                        .capacity(a.getCapacity())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

    public GetDeviceInfoResponse getDeviceInfo() {
        log.info("DeviceService.getDeviceInfo");

        List<DeviceJpaEntity> deviceJpaEntityList = deviceRepository.findAll();

        return GetDeviceInfoResponse.builder()
                .deviceInfos(
                        deviceJpaEntityList.stream()
                                .map(a -> GetDeviceOnMapResponse.DeviceInfo.builder()
                                        .device_id(a.getDeviceId())
                                        .location(a.getLocation())
                                        .latitude(a.getLatitude())
                                        .longitude(a.getLongitude())
                                        .capacity(a.getCapacity())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}

