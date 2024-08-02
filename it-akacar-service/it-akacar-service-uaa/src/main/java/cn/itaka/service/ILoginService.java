package cn.itaka.service;

import cn.itaka.pojo.domain.Login;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录表 服务类
 * </p>
 *
 * @author xp
 * @since 2024-08-01
 */
public interface ILoginService extends IService<Login> {

    void registerSaveLogin(RegisterSaveLoginParam registerSaveLoginParam);
}
