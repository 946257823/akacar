package cn.itaka.service;

import cn.itaka.pojo.domain.Driver;
import cn.itaka.pojo.domain.DriverAuthMaterial;
import cn.itaka.pojo.dto.DriverRealAuthAuditDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 司机实名资料 服务类
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
public interface IDriverAuthMaterialService extends IService<DriverAuthMaterial> {

    void realAuthAudit(DriverRealAuthAuditDto driverRealAuthAuditDto);

    void saveRealAuth(DriverAuthMaterial driverAuthMaterial);


}
