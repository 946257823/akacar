package cn.itaka.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.itaka.constants.GlobalExceptionCode;
import cn.itaka.mapper.DriverAuthMaterialMapper;
import cn.itaka.pojo.domain.Driver;
import cn.itaka.pojo.domain.DriverAuthMaterial;
import cn.itaka.pojo.domain.DriverMaterialAuthLog;
import cn.itaka.pojo.dto.DriverRealAuthAuditDto;
import cn.itaka.service.IDriverAuthMaterialService;
import cn.itaka.service.IDriverMaterialAuthLogService;
import cn.itaka.service.IDriverService;
import cn.itaka.utils.AssertUtil;
import cn.itaka.utils.BitStatesUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 司机实名资料 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
@Service
public class DriverAuthMaterialServiceImpl extends ServiceImpl<DriverAuthMaterialMapper, DriverAuthMaterial> implements IDriverAuthMaterialService {

    @Autowired
    private IDriverService driverService;

    @Autowired
    private IDriverMaterialAuthLogService driverMaterialAuthLogService;

    /**
     * 司机实名信息审核
     * @param driverRealAuthAuditDto
     */
    @Override
    public void realAuthAudit(DriverRealAuthAuditDto driverRealAuthAuditDto) {

        // 实名信息是否存在
        Long id = driverRealAuthAuditDto.getId();
        DriverAuthMaterial driverAuthMaterial = super.getById(id);
        AssertUtil.isNotNull(driverAuthMaterial,GlobalExceptionCode.SERVICE_ERROR);
        // 司机是否已经审核通过
        Long driverId = driverAuthMaterial.getDriverId();
        DriverAuthMaterial driverAuthMaterialPass = super.getOne(new LambdaQueryWrapper<DriverAuthMaterial>()
                .eq(DriverAuthMaterial::getDriverId, driverId)
                .eq(DriverAuthMaterial::getRealAuthStatus, 1));
        AssertUtil.isNull(driverAuthMaterialPass,GlobalExceptionCode.SERVICE_ERROR);

        // 修改实名信息状态
        Boolean approve = driverRealAuthAuditDto.getApprove();
        String remark = driverRealAuthAuditDto.getRemark();
        Date date = new Date();
        long loginIdAsLong = StpUtil.getLoginIdAsLong();

        // 修改司机位状态
        Driver driver = driverService.getById(driverId);

        if (approve) {
            driverAuthMaterial.setRealAuthStatus(1);


            driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
        }else {
            // 不通过
            driverAuthMaterial.setRealAuthStatus(2);
            driver.setBitState(BitStatesUtil.removeState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
        }

        driverService.updateById(driver);

        driverAuthMaterial.setUpdateTime(date);
        driverAuthMaterial.setAuditTime(date);
        driverAuthMaterial.setAuditRemark(remark);
        driverAuthMaterial.setAuditUserId(loginIdAsLong);
        super.updateById(driverAuthMaterial);
        // 添加审核日志
        DriverMaterialAuthLog driverMaterialAuthLog = BeanUtil.copyProperties(driverAuthMaterial, DriverMaterialAuthLog.class);
        driverMaterialAuthLog.setId(null);
        driverMaterialAuthLog.setAuthMaterialId(id);
        driverMaterialAuthLog.setCreateTime(date);
        driverMaterialAuthLog.setDeleted(false);
        driverMaterialAuthLog.setVersion(0);
        driverMaterialAuthLogService.save(driverMaterialAuthLog);
    }

    /**
     * 司机实名认证
     * @param authMaterial
     */
    @Override
    public void saveRealAuth(DriverAuthMaterial authMaterial) {
        // 1.参数校验
        // 2.业务校验
        // 2.1当前司机是否有审核中或者审核成功的数据，如果有则不允许提交
        long driverId = StpUtil.getLoginIdAsLong();
        List<DriverAuthMaterial> list = super.list(new LambdaQueryWrapper<DriverAuthMaterial>()
                .eq(DriverAuthMaterial::getDriverId, driverId)
                .in(DriverAuthMaterial::getRealAuthStatus, Arrays.asList(0, 1))
        );
        AssertUtil.isTrue(CollUtil.isEmpty(list), GlobalExceptionCode.SERVICE_ERROR);

        // 3.业务实现
        // 3.1保存实名信息
        authMaterial.setRealAuthStatus(0);
        authMaterial.setDriverId(driverId);
        authMaterial.setCreateTime(new Date());
        authMaterial.setDeleted(false);
        authMaterial.setVersion(0);
        // 3.2 修改driver的位状态
        Driver driver = driverService.getById(driverId);
        AssertUtil.isNotNull(driver, GlobalExceptionCode.SERVICE_ERROR);
        driver.setBitState(BitStatesUtil.addState(driver.getBitState(),BitStatesUtil.OP_REAL_AUTHENTICATING));
        driverService.updateById(driver);


    }


}
