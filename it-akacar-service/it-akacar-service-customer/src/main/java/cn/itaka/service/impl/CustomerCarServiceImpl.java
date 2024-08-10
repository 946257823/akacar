package cn.itaka.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.itaka.mapper.CustomerCarMapper;
import cn.itaka.pojo.domain.CustomerCar;
import cn.itaka.pojo.domain.dto.CarDto;
import cn.itaka.service.ICustomerCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 客户车辆 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class CustomerCarServiceImpl extends ServiceImpl<CustomerCarMapper, CustomerCar> implements ICustomerCarService {

    /**
     * 保存车辆信息
     * @param carDto
     */
    @Override
    public void addCar(CarDto carDto) {
        CustomerCar customerCar = new CustomerCar();
        customerCar.setCustomerId(StpUtil.getLoginIdAsLong());
        customerCar.setCarPlate(carDto.getCarPlate());
        customerCar.setCarType(carDto.getCarType());
        customerCar.setCreateTime(new Date());
        customerCar.setVersion(0);
        customerCar.setDeleted(false);
        super.save(customerCar);
    }
}
