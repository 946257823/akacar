package cn.itaka.service;

import cn.itaka.pojo.domain.Driver;
import cn.itaka.pojo.domain.DriverAuthMaterial;
import cn.itaka.pojo.dto.DriverDaySummaryDto;
import cn.itaka.pojo.dto.DriverLocationToGeoDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机对象 服务类
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
public interface IDriverService extends IService<Driver> {

//    void register(String openIdCode, String phoneCode);
    void register(String openIdCode);

    DriverAuthMaterial getRealAuthInfo();


    DriverDaySummaryDto getDaySummary();

    void online();

    void offline();

    void changeLocationToGeo(DriverLocationToGeoDto driverLocationToGeoDto);
}
