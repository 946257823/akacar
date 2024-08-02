package cn.itaka.controller.app;

import cn.itaka.result.R;
import cn.itaka.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "小程序乘客对象",description = "小程序乘客对象")
@RestController
@RequestMapping("/app/customer")
public class AppCustomerController {

    @Autowired
    public ICustomerService customerService;
    /**
     * 小程序乘客注册
     * @param openIdCode
     * @return
     */
//    @GetMapping("/register/{openIdCode}/{phoneCode}")
    @GetMapping("/register/{openIdCode}")
    @Operation(summary = "小程序乘客注册", description = "小程序乘客注册接口")
    @Parameter(name = "openIdCode", description = "获取openIdCode", required = true)
//    public R<Void> register(@PathVariable String openIdCode, @PathVariable String phoneCode) {
    public R<Void> register(@PathVariable String openIdCode) {
        customerService.register(openIdCode);
        return R.success();
    }
}
