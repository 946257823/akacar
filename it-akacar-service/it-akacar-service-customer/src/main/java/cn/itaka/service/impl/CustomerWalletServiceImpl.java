package cn.itaka.service.impl;

import cn.itaka.pojo.domain.CustomerWallet;
import cn.itaka.mapper.CustomerWalletMapper;
import cn.itaka.service.ICustomerWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 乘客的钱包 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class CustomerWalletServiceImpl extends ServiceImpl<CustomerWalletMapper, CustomerWallet> implements ICustomerWalletService {

}
