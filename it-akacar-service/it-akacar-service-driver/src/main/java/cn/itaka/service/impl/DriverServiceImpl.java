package cn.itaka.service.impl;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.itaka.constants.GlobalExceptionCode;
import cn.itaka.exception.GlobalException;
import cn.itaka.mapper.DriverMapper;
import cn.itaka.pojo.domain.Driver;
import cn.itaka.pojo.domain.DriverSetting;
import cn.itaka.pojo.domain.DriverSummary;
import cn.itaka.pojo.domain.DriverWallet;
import cn.itaka.pojo.dto.CodeToOpenIdDto;
import cn.itaka.service.IDriverService;
import cn.itaka.service.IDriverSettingService;
import cn.itaka.service.IDriverSummaryService;
import cn.itaka.service.IDriverWalletService;
import cn.itaka.utils.AssertUtil;
import cn.itaka.utils.JsonToClassUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





/**
 * <p>
 * 司机对象 服务实现类
 * </p>
 *
 * @author xp
 * @since 2024-07-28
 */
@Service
@Transactional // 支持事务回滚
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {

    @Autowired
    private IDriverWalletService iDriverWalletService;

    @Autowired
    private IDriverSettingService iDriverSettingService;

    @Autowired
    private IDriverSummaryService iDriverSummaryService;


    /**
     *
     * @param openIdCode
     * Vo:后端返回给前端的临时对象
     * Dto
     */
    @Override
    public void register(String openIdCode) {
        // 1.参数校验
        // 2.业务校验
        // 3.业务实现

        // 1. 先根据openIdCode拿到OpenId
        String urltemplate = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        String url = String.format(urltemplate,"wx15ecfcee13cde076","8e78f62669f0c67a82220cddb813e365",openIdCode);
        // 获取到返回参数,类型为Json字符串
        String jsonResultStr = HttpUtil.get(url);
        System.out.println(jsonResultStr);
        // 将返回的json字符串转为CodeToOpenIdDto对象
        CodeToOpenIdDto codeToOpenIdDto = JsonToClassUtil.objJsonStr2Class(jsonResultStr, CodeToOpenIdDto.class);
        // if判断如果是抛异常，可以使用断言工具类
        // 希望在不空的时候抛异常
        AssertUtil.isNotEmpty(codeToOpenIdDto.getOpenid(),GlobalExceptionCode.PARAM_CODE_ERROR);
//        if (StrUtil.isBlank(codeToOpenIdDto.getOpenid())) {
//            throw new GlobalException(GlobalExceptionCode.PARAM_CODE_ERROR);
//        }

        String openId = codeToOpenIdDto.getOpenid();
        // 获取到openId之后，去数据库查看司机账号是否已经注册
        Driver driver = super.getOne(new QueryWrapper<Driver>().eq("open_id", openId));

        AssertUtil.isNotNull(driver,GlobalExceptionCode.PARAM_PHONE_EXIST);
//        if (ObjectUtil.isNotEmpty(driver)) {
//            throw new GlobalException(GlobalExceptionCode.PARAM_PHONE_EXIST);
//        }
        // 3 业务实现
        // 3.1 保存driver信息
        driver = new Driver();
        saveDriver(driver,openId);

        Long id = driver.getId();
        // 3.2 初始化从表
        initDriverAssociation(id);
    }

    private void initDriverAssociation(Long id) {
        DriverWallet driverWallet = new DriverWallet();
        driverWallet.setId(id);
        driverWallet.setAmount(new BigDecimal("0"));
        driverWallet.setPassword("123456");
        driverWallet.setDeleted(false);
        driverWallet.setVersion(0);
        driverWallet.setCreateTime(new Date());
        iDriverWalletService.save(driverWallet);


        DriverSetting driverSetting = new DriverSetting();
        driverSetting.setId(id);
        driverSetting.setAutoAccept(false);
        driverSetting.setOrientation(false);
        driverSetting.setListenService(false);
        driverSetting.setOrderDistance(50);
        driverSetting.setRangeDistance(50);
        driverSetting.setCreateTime(new Date());
        driverSetting.setDeleted(false);
        driverSetting.setVersion(0);
        iDriverSettingService.save(driverSetting);

        DriverSummary driverSummary = new DriverSummary();
        driverSummary.setId(id);
        driverSummary.setLevel(0);
        driverSummary.setTotalOrders(0);
        driverSummary.setWeekOrders(0);
        driverSummary.setGoodComments(0);
        driverSummary.setAppeal(0);
        driverSummary.setTotalComplaint(0);
        driverSummary.setTodayTotalCancel(0);
        driverSummary.setDriveDuration(0);
        driverSummary.setTradeOrders(0);
        driverSummary.setTodayTradeOrders(0);
        driverSummary.setTodayComplaint(0);
        driverSummary.setTodayCancel(0);
        driverSummary.setTodayIncome(new BigDecimal("0"));
        driverSummary.setCreateTime(new Date());
        driverSummary.setDeleted(false);
        driverSummary.setVersion(0);
        iDriverSummaryService.save(driverSummary);

    }

    /**
     *
     * @param driver
     */
    private void saveDriver(Driver driver,String openId) {
        // @todo 明天写
//        driver.setPhone("");
        driver.setArchive(false);
        driver.setCreateTime(new Date());
        driver.setDeleted(false);
        driver.setVersion(0);
        driver.setOpenId(openId);
        // @todo 明天写
        // driver.setBitState(0L);
        super.save(driver);
    }
}
