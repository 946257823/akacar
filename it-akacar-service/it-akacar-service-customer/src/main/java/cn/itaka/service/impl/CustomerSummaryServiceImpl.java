package cn.itaka.service.impl;

import cn.itaka.pojo.domain.CustomerSummary;
import cn.itaka.mapper.CustomerSummaryMapper;
import cn.itaka.service.ICustomerSummaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 乘客数据汇总 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class CustomerSummaryServiceImpl extends ServiceImpl<CustomerSummaryMapper, CustomerSummary> implements ICustomerSummaryService {

}
