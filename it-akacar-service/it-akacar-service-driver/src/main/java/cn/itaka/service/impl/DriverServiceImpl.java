package cn.itaka.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.itaka.constants.Constants;
import cn.itaka.constants.GlobalExceptionCode;
import cn.itaka.exception.GlobalException;
import cn.itaka.feign.AppLoginFeignClient;
import cn.itaka.mapper.DriverAuthMaterialMapper;
import cn.itaka.mapper.DriverMapper;
import cn.itaka.pojo.domain.*;
import cn.itaka.pojo.dto.DriverDaySummaryDto;
import cn.itaka.pojo.dto.DriverLocationToGeoDto;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.service.*;
import cn.itaka.template.AppWechatTemplate;
import cn.itaka.utils.AssertUtil;
import cn.itaka.utils.BitStatesUtil;
import cn.itaka.utils.NameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    private AppWechatTemplate appWechatTemplate;

    @Autowired
    private AppLoginFeignClient appLoginFeignClient;

    @Autowired
    private DriverAuthMaterialMapper driverAuthMaterialMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @param openIdCode
     * Vo:后端返回给前端的临时对象
     * Dto
     */
    @Override
//    public void register(String openIdCode, String phoneCode) {
    public void register(String openIdCode) {
        // 1.参数校验
        // 2.业务校验
        // 3.业务实现
        // 1. 先根据openIdCode拿到OpenId
        String openId = appWechatTemplate.getOpenIdByCode(openIdCode);
        // 获取到openId之后，去数据库查看司机账号是否已经注册
        Driver driver = super.getOne(new QueryWrapper<Driver>().eq("open_id", openId));
//        AssertUtil.isNotNull(driver,GlobalExceptionCode.PARAM_PHONE_EXIST);
        if (ObjectUtil.isNotEmpty(driver)) {
            throw new GlobalException(GlobalExceptionCode.PARAM_PHONE_EXIST);
        }
        // 3 业务实现
        // 获取手机号
        String phone = appWechatTemplate.getPhoneByAccessToken("phoneCode");
        // 3.1 保存driver信息
        driver = new Driver();
        saveDriver(driver,openId);
        Long id = driver.getId();
        // 3.2 初始化从表
        initDriverAssociation(id);

        // 保存login
        saveLogin(id,openId,phone);

    }
/**
 *查询司机实名认证信息
 */
    @Override
   public DriverAuthMaterial getRealAuthInfo() {
       DriverAuthMaterial driverAuthMaterial = driverAuthMaterialMapper.selectOne(new LambdaQueryWrapper<DriverAuthMaterial>()
               .eq(DriverAuthMaterial::getDriverId, StpUtil.getLoginIdAsLong())
               .orderByDesc(DriverAuthMaterial::getCreateTime)
               .last("limit 1"));
       return driverAuthMaterial;
   }

    @Override
    public DriverDaySummaryDto getDaySummary() {
        DriverSummary driverSummary = iDriverSummaryService.getById(StpUtil.getLoginIdAsLong());
        AssertUtil.isNotNull(driverSummary,GlobalExceptionCode.SERVICE_ERROR);
        return BeanUtil.copyProperties(driverSummary,DriverDaySummaryDto.class);
    }

    /**
     * 司机上线
     */
    @Override
    public void online() {
        // 1.业务校验 1.查询司机的配置，判断是否存在
        long loginId = StpUtil.getLoginIdAsLong();
        DriverSetting driverSetting = iDriverSettingService.getById(loginId);
        AssertUtil.isNotNull(driverSetting,GlobalExceptionCode.SERVICE_ERROR);
        // 业务实现 1.把司机配置信息存储到Redis中
        String key = String.format(Constants.Redis.DRIVER_ONLINE_KEY, loginId);
        // @TODO:上线之前把过期时间改为30分钟
        redisTemplate.opsForValue().set(key,driverSetting,10, TimeUnit.HOURS);

    }
    /**
     * 司机下线
     */
    @Override
    public void offline() {
        // 1.删除司机的redis配置
        long loginId = StpUtil.getLoginIdAsLong();
        String key = String.format(Constants.Redis.DRIVER_ONLINE_KEY, loginId);
        redisTemplate.delete(key);
        // 2.删除司机的GEO位置信息
        redisTemplate.opsForGeo().remove(Constants.Redis.DRIVER_LOCATION_KEY,loginId);
    }



    @Override
    public void changeLocationToGeo(DriverLocationToGeoDto driverLocationToGeoDto) {
        // 1.参数校验JSR303
        // 2.业务校验-司机的配置在redis中是否存在，如果不存在则不缓存
        String loginId = StpUtil.getLoginIdAsString(); // redis的key必须位string！
        String key = String.format(Constants.Redis.DRIVER_ONLINE_KEY, loginId);
        Object driverSetting = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNull(driverSetting)) {
            return;
        }
        // 3.业务实现 缓存司机的位置到Geo
        redisTemplate.opsForGeo().add(
                Constants.Redis.DRIVER_LOCATION_KEY,
                new Point(driverLocationToGeoDto.getLongitude(), driverLocationToGeoDto.getLatitude())
                , StpUtil.getLoginIdAsString());
    }

    /**
     * 司机实名认证
     * @param driver
     */



    /**
     * 保存login
     */
    private void saveLogin(Long id, String openId, String phone) {
        // 3.3 调用feign接口保存Login
        RegisterSaveLoginParam registerSaveLoginParam = new RegisterSaveLoginParam();
        registerSaveLoginParam.setUsername(phone);
        registerSaveLoginParam.setPassword("123456");
        registerSaveLoginParam.setType(Constants.Login.TYPE_DRIVER);
        registerSaveLoginParam.setEnabled(true);
        registerSaveLoginParam.setAvatar("");
        registerSaveLoginParam.setAdmin(false);
        String name = NameUtil.getName();
        registerSaveLoginParam.setNickName(name);
        registerSaveLoginParam.setOpenId(openId);
        registerSaveLoginParam.setName(name);
        registerSaveLoginParam.setPhone(phone);
        registerSaveLoginParam.setId(id);

        appLoginFeignClient.save(registerSaveLoginParam);
    }

    private String getPhoneByPhoneCode(String phoneCode) {
        // 1.获取access_token
        String accessTokenUrl = String.format(Constants.Url.ACCESS_TOKEN_URL_TEMPLATE, Constants.appid, Constants.secret);
        String resultJsonStr = HttpUtil.get(accessTokenUrl);
//        JSONObject accessTokenJson = JSONUtil.parseObj(resultJsonStr);
        JSONObject accessTokenResult = JSONUtil.toBean(resultJsonStr, JSONObject.class);
        String accessToken = accessTokenResult.getStr("access_token");
        AssertUtil.isNotEmpty(accessToken,GlobalExceptionCode.PARAM_CODE_ERROR);

        // 2.获取手机号
        String phoneUrl = String.format(Constants.Url.CODE_TO_PHONE_URL_TEMPLATE, accessToken);
        HashMap<String, Object> body = new HashMap<>();
        body.put("code", phoneCode);
        String resultPhoneStr = HttpUtil.post(phoneUrl, body);

        JSONObject resultJsonObj = JSONUtil.parseObj(resultPhoneStr);
        Integer errcode = resultJsonObj.getInt("errcode");
        AssertUtil.isEquals(0,errcode, GlobalExceptionCode.PARAM_CODE_ERROR);

        JSONObject phoneInfoObj = resultJsonObj.getJSONObject("phone_info");
        String phoneNumber = phoneInfoObj.getStr("phoneNumber");
        AssertUtil.isNotEmpty(phoneNumber,GlobalExceptionCode.PARAM_CODE_ERROR);
        return phoneNumber;
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





//    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
//        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
//        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
//        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
//                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
//                .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
//                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
//                .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
//        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
//        config.endpoint = "dysmsapi.aliyuncs.com";
//        return new com.aliyun.dysmsapi20170525.Client(config);
//    }

//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        com.aliyun.dysmsapi20170525.Client client = Sample.createClient();
//        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            client.sendSmsWithOptions(sendSmsRequest, runtime);
//        } catch (TeaException error) {
//            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//            // 错误 message
//            System.out.println(error.getMessage());
//            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        } catch (Exception _error) {
//            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//            // 错误 message
//            System.out.println(error.getMessage());
//            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        }
//    }

    /**
     *
     * @param driver
     */
    private void saveDriver(Driver driver,String openId) {
//        driver.setPhone("");
        driver.setArchive(false);
        driver.setCreateTime(new Date());
        driver.setDeleted(false);
        driver.setVersion(0);
        driver.setOpenId(openId);
        driver.setBitState(BitStatesUtil.OP_PHONE);
        super.save(driver);
    }
}