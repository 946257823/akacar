package cn.itaka.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.itaka.pojo.domain.CustomerCar;
import cn.itaka.pojo.domain.dto.CarDto;
import cn.itaka.result.R;
import cn.itaka.service.ICustomerCarService;
import cn.itaka.service.ICustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Tag(name = "小程序乘客对象",description = "小程序乘客对象")
@RestController
@RequestMapping("/app/customer")
public class AppCustomerController {

    @Autowired
    public ICustomerService customerService;

    @Autowired
    private ICustomerCarService customerCarService;
    /**
     * 小程序乘客注册
     * @param openIdCode
     * @return
     */
//    @GetMapping("/register/{openIdCode}/{phoneCode}")
    @GetMapping("/register/{openIdCode}")
    @Operation(summary = "小程序乘客注册", description = "小程序乘客注册接口")
    @Parameter(name = "openIdCode", description = "获取openIdCode", required = true)
    @SaIgnore
//    public R<Void> register(@PathVariable String openIdCode, @PathVariable String phoneCode) {
    public R<Void> register(@PathVariable String openIdCode) {
        customerService.register(openIdCode);
        return R.success();
    }

    /**
     * 查询乘客车辆信息
     * @param
     * @return
     */
    @GetMapping(value = "/car/list")
    @Operation(summary = "查询乘客车辆信息", description = "查询乘客车辆信息接口")
    public R<List<CustomerCar>> getCarList() {
        return R.success(customerCarService.list(new LambdaQueryWrapper<CustomerCar>()
                .eq(CustomerCar::getCustomerId, StpUtil.getLoginIdAsLong())
        ));
    }

    /**
     * 保存车辆信息
     * @param
     * @return
     */
    @GetMapping(value = "/car/add")
    @Operation(summary = "保存车辆信息", description = "保存车辆信息接口")
    @Parameter(name = "carDto",description = "保存的对象",required = true)
    public R<Void> addCar(@RequestBody @Valid CarDto carDto) {
        customerCarService.addCar(carDto);
        return R.success();
    }






}
