package cn.itaka.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itaka.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "订单控制器",description = "订单控制器对象")
@RestController
@RequestMapping("/app/order")
public class AppOrderController {

    @Autowired
    public IOrderService orderService;

    @GetMapping("/createOrder")
    @Operation(summary = "乘客下单", description = "乘客下单接口")
    @Parameter(name = "createOrderDto", description = "下单参数对象", required = true)
    public R<Void> register(@RequestBody @Valid CreateOrderDto createOrderDto) {
        orderService.createOrder(createOrderDto);
        return R.success();
    }
}
