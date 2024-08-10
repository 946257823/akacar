package cn.itaka.controller.manager.app;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.itaka.pojo.domain.Login;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.pojo.vo.LoginVo;
import cn.itaka.result.R;
import cn.itaka.service.ILoginService;
import cn.itaka.template.AppWechatTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "小程序登录表",description = "小程序登录表")
@RestController
@RequestMapping("/app/login")
public class AppLoginController {

    @Autowired
    public ILoginService loginService;

    @Operation( summary= "微信登录",description = "微信登录接口")
    @PostMapping("/wxLogin/{code}/{type}")
    @SaIgnore
    public R<LoginVo> wxLogin(@PathVariable("code") String code, @PathVariable("type") Integer type){
        return R.success(loginService.wxLogin(code, type));
    }

    @Operation( summary= "注册保存Login",description = "注册保存Login接口")
    @Parameter(name = "login",description = "保存的对象",required = true)
    @PostMapping
    @SaIgnore
    public R<Void> save(@RequestBody @Valid RegisterSaveLoginParam registerSaveLoginParam){
        loginService.registerSaveLogin(registerSaveLoginParam);
        return R.success();
    }

}
