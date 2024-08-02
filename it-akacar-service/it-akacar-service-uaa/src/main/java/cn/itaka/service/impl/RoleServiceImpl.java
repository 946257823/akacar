package cn.itaka.service.impl;

import cn.itaka.pojo.domain.Role;
import cn.itaka.mapper.RoleMapper;
import cn.itaka.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
