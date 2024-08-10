package cn.itaka.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.itaka.pojo.domain.DriverAuthMaterial;
import cn.itaka.pojo.dto.DriverDaySummaryDto;
import cn.itaka.pojo.dto.DriverLocationToGeoDto;
import cn.itaka.result.R;
import cn.itaka.service.IDriverAuthMaterialService;
import cn.itaka.service.IDriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "小程序司机对象",description = "小程序司机对象")
@RestController
@RequestMapping("/app/driver")
public class AppDriverController{

    @Autowired
    public IDriverService driverService;

    @Autowired
    private IDriverAuthMaterialService driverAuthMaterialService;
    /**
     * 小程序司机注册
     * @param openIdCode
     * @return
     */
//    @GetMapping("/register/{openIdCode}/{phoneCode}")
    @GetMapping("/register/{openIdCode}")
    @Operation(summary = "小程序司机注册", description = "小程序司机注册接口")
    @Parameter(name = "openIdCode", description = "获取openIdCode", required = true)
    @SaIgnore
//    public R<Void> register(@PathVariable String openIdCode, @PathVariable String phoneCode) {
    public R<Void> register(@PathVariable String openIdCode) {
        driverService.register(openIdCode);
        return R.success();
    }


    @Operation( summary= "小程序司机实名认证",description = "小程序司机实名认证接口")
    @Parameter(name = "driverAuthMaterial",description = "保存的对象",required = true)
    @PostMapping("/saveRealAuth")
    public R<Void> saveRealAuth(@RequestBody @Valid DriverAuthMaterial driverAuthMaterial){
        driverAuthMaterialService.saveRealAuth(driverAuthMaterial);
        return R.success();
    }

    @Operation( summary= "获取司机当日数据",description = "获取司机当日数据接口")
    @GetMapping("/daySummary")
    public R<DriverDaySummaryDto> getDaySummary(){
        return R.success(driverService.getDaySummary());
    }


    @Operation( summary= "司机上线",description = "司机上线接口")
    @PostMapping("/online")
    public R<Void> online(){
        driverService.online();
        return R.success();
    }

    @Operation( summary= "司机下线",description = "司机下线接口")
    @PostMapping("/offline")
    public R<Void> offline(){
        driverService.offline();
        return R.success();
    }


    @Operation( summary= "司机实时位置更新到GEO",description = "司机实时位置更新到GEO接口")
    @Parameter(name = "driverLocationToGeo",description = "司机位置坐标",required = true)
    @PostMapping("/locationToGeo")
    public R<Void> changeLocationToGeo(@RequestBody DriverLocationToGeoDto driverLocationToGeoDto){
        driverService.changeLocationToGeo(driverLocationToGeoDto);
        return R.success();
    }
}
