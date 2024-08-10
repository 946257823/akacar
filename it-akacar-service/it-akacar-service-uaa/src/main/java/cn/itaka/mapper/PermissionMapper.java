package cn.itaka.mapper;

import cn.itaka.pojo.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectByLoginId(Long loginId);
}
