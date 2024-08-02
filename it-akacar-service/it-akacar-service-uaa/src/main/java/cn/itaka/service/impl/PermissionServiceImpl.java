package cn.itaka.service.impl;

import cn.itaka.pojo.domain.Permission;
import cn.itaka.mapper.PermissionMapper;
import cn.itaka.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
