package cn.itaka.service.impl;

import cn.itaka.pojo.domain.CustomerFine;
import cn.itaka.mapper.CustomerFineMapper;
import cn.itaka.service.ICustomerFineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 乘客罚款 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class CustomerFineServiceImpl extends ServiceImpl<CustomerFineMapper, CustomerFine> implements ICustomerFineService {

}
