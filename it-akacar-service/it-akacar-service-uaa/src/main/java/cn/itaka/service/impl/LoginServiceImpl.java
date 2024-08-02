package cn.itaka.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.itaka.pojo.domain.Login;
import cn.itaka.mapper.LoginMapper;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {
    /**
     * 小程序注册保存Login
     * @param registerSaveLoginParam
     */
    @Override
    public void registerSaveLogin(RegisterSaveLoginParam registerSaveLoginParam) {
        Login login = new Login();
        BeanUtil.copyProperties(registerSaveLoginParam,login,false);
        login.setCreateTime(new Date());
        login.setVersion(0);
        login.setDeleted(false);
        super.save(login);
    }
}
