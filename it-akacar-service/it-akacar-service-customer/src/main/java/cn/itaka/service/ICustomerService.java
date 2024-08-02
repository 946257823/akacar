package cn.itaka.service;

import cn.itaka.pojo.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
public interface ICustomerService extends IService<Customer> {

    void register(String openIdCode);
}
