package cn.itaka.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationToGeoDto {

    private Double longitude;
    private Double latitude;
}
