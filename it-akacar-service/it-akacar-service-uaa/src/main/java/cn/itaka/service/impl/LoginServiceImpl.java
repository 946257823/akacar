package cn.itaka.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import cn.itaka.constants.Constants;
import cn.itaka.constants.GlobalExceptionCode;
import cn.itaka.exception.GlobalException;
import cn.itaka.mapper.PermissionMapper;
import cn.itaka.pojo.domain.Login;
import cn.itaka.mapper.LoginMapper;
import cn.itaka.pojo.domain.Permission;
import cn.itaka.pojo.dto.AdminLoginDto;
import cn.itaka.pojo.dto.LoginInfoDto;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.pojo.vo.LoginVo;
import cn.itaka.properties.SaTokenSettingProperties;
import cn.itaka.service.ILoginService;
import cn.itaka.template.AppWechatTemplate;
import cn.itaka.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginContext;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private AppWechatTemplate appWechatTemplate;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SaTokenSettingProperties saTokenSettingProperties;
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

    @Override
    public LoginVo wxLogin(String code ,Integer type) {
        // 1.参数校验JSR303
        // 2.业务校验
        // 2.1.openId是否能获取到
        String openid = appWechatTemplate.getOpenIdByCode(code);
        // 2.2.当前用户是否注册过
        Login login = super.getOne(new LambdaQueryWrapper<Login>()
                .eq(Login::getOpenId, openid)
                .eq(Login::getType, type));
        AssertUtil.isNotNull(login, GlobalExceptionCode.SERVICE_ERROR);
        // 3.业务实现
        // 3.1.使用satoken登录
        Long loginId = login.getId();
        StpUtil.login(loginId);

        // 3.2.使用satoen获取token
        String tokenName = StpUtil.getTokenName();
        String tokenValue = StpUtil.getTokenValue();
        // 3.3.查询当前用户的权限信息
        List<String> permissions = permissionMapper.selectByLoginId(loginId);
        // 3.4.将权限信息存入到redis中  permission-key:%s
        String key = String.format(saTokenSettingProperties.getPermissionKey(), loginId);
        redisTemplate.opsForValue().set(key,permissions,StpUtil.getTokenTimeout(),TimeUnit.SECONDS);
        // 3.5.返回token，权限信息，用户信息
        LoginInfoDto loginInfoDto = BeanUtil.copyProperties(login, LoginInfoDto.class);
        return new LoginVo(tokenName,tokenValue,permissions,loginInfoDto);
    }

    /**
     * 后台登录实现
     * @param adminLoginDto
     * @return
     */
    @Override
    public LoginVo adminLogin(AdminLoginDto adminLoginDto) {
        // 1.参数校验JSR303
        // 2.业务校验
        // 2.1账号是否存在
        Login login = super.getOne(new LambdaQueryWrapper<Login>()
                .eq(Login::getUsername, adminLoginDto.getUsername())
                .eq(Login::getType, adminLoginDto.getType()));
        // 2.2密码是否正确
        if(!login.getPassword().equals(adminLoginDto.getPassword())) {
            throw new GlobalException(GlobalExceptionCode.SERVICE_ERROR);
        }
        // 3.业务实现
        // 3.1 使用sa-token登录
        Long loginId = login.getId();
        StpUtil.login(loginId);

        // 3.2 获取tokenName，tokenValue
        String tokenName = StpUtil.getTokenName();
        String tokenValue = StpUtil.getTokenValue();

        // 3.3 获取权限存到redis
        List<String> permissions = permissionMapper.selectByLoginId(loginId);
        String key = String.format(saTokenSettingProperties.getPermissionKey(), loginId);
        redisTemplate.opsForValue().set(key,permissions,StpUtil.getTokenTimeout(),TimeUnit.SECONDS);

        // 3.4 将用户信息装到loginContext
        LoginInfoDto loginInfoDto = BeanUtil.copyProperties(login, LoginInfoDto.class);

        // 3.5 封装到loginvo返回
        return new LoginVo(tokenName,tokenValue,permissions,loginInfoDto);
    }
}
