package cn.itaka.controller.app;

import cn.itaka.service.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import cn.itaka.result.R;


@Tag(name = "小程序司机对象",description = "小程序司机对象")
@RestController
@RequestMapping("/app/driver")
public class AppDriverController{

    @Autowired
    public IDriverService driverService;
    /**
     * 小程序司机注册
     * @param openIdCode
     * @return
     */
//    @GetMapping("/register/{openIdCode}/{phoneCode}")
    @GetMapping("/register/{openIdCode}")
    @Operation(summary = "小程序司机注册", description = "小程序司机注册接口")
    @Parameter(name = "openIdCode", description = "获取openIdCode", required = true)
//    public R<Void> register(@PathVariable String openIdCode, @PathVariable String phoneCode) {
    public R<Void> register(@PathVariable String openIdCode) {
        driverService.register(openIdCode);
        return R.success();
    }
}
