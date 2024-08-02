package cn.itaka.service.impl;

import cn.itaka.pojo.domain.Menu;
import cn.itaka.mapper.MenuMapper;
import cn.itaka.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
