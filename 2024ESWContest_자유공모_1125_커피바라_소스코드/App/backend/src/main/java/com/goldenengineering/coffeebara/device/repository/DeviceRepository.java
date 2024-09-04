package com.goldenengineering.coffeebara.device.repository;

import com.goldenengineering.coffeebara.device.DeviceController;
import com.goldenengineering.coffeebara.device.model.DeviceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceJpaEntity, Long> {
    DeviceJpaEntity findByDeviceId(long id);
    List<DeviceJpaEntity> findByLatitudeBetweenAndLongitudeBetween(Double southLeftLatitude, Double northRightLatitude, Double southLeftLongitude, Double northRightLongitude);
    List<DeviceJpaEntity> findAllBy();
}
