package cn.itaka.service.impl;

import cn.itaka.pojo.domain.Driver;
import cn.itaka.mapper.DriverMapper;
import cn.itaka.service.IDriverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 司机对象 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
@Service
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {

}
