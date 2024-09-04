package com.goldenengineering.coffeebara.device.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceJpaEntity {

    @Id
    @Column(name = "device_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;
}
