package cn.itaka.service.impl;

import cn.itaka.pojo.domain.CustomerCar;
import cn.itaka.mapper.CustomerCarMapper;
import cn.itaka.service.ICustomerCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
