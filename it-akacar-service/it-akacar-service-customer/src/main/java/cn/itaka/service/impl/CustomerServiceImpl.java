package cn.itaka.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.itaka.constants.Constants;
import cn.itaka.constants.GlobalExceptionCode;
import cn.itaka.exception.GlobalException;
import cn.itaka.feign.AppLoginFeignClient;
import cn.itaka.mapper.CustomerMapper;
import cn.itaka.pojo.domain.Customer;
import cn.itaka.pojo.domain.CustomerSummary;
import cn.itaka.pojo.domain.CustomerWallet;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.service.ICustomerService;
import cn.itaka.service.ICustomerSummaryService;
import cn.itaka.service.ICustomerWalletService;
import cn.itaka.template.AppWechatTemplate;
import cn.itaka.utils.AssertUtil;
import cn.itaka.utils.BitStatesUtil;
import cn.itaka.utils.NameUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    private ICustomerWalletService iCustomerWalletService;

    @Autowired
    private ICustomerSummaryService iCustomerSummaryService;

    @Autowired
    private AppWechatTemplate appWechatTemplate;

    @Autowired
    private AppLoginFeignClient appLoginFeignClient;
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
        Customer customer = super.getOne(new QueryWrapper<Customer>().eq("open_id", openId));
//        AssertUtil.isNotNull(customer,GlobalExceptionCode.PARAM_PHONE_EXIST);
        if (ObjectUtil.isNotEmpty(customer)) {
            throw new GlobalException(GlobalExceptionCode.PARAM_PHONE_EXIST);
        }
        // 3 业务实现
        // 获取手机号
        String phone = appWechatTemplate.getPhoneByAccessToken("phoneCode");
        // 3.1 保存customer信息
        customer = new Customer();
        saveCustomer(customer,openId);
        Long id = customer.getId();
        String password = "123456";
        // 3.2 初始化从表
        initCustomerAssociation(id,password);

        // 保存login
        saveLogin(openId,phone,password,Constants.Login.TYPE_DRIVER);

    }

    /**
     * 保存login
     */
    private void saveLogin(String openId, String phone,String password,Integer type) {
        // 3.3 调用feign接口保存Login
        RegisterSaveLoginParam registerSaveLoginParam = new RegisterSaveLoginParam();
        registerSaveLoginParam.setUsername(phone);
        registerSaveLoginParam.setPassword(password);
        registerSaveLoginParam.setType(type);
        registerSaveLoginParam.setEnabled(true);
        registerSaveLoginParam.setAvatar("");
        registerSaveLoginParam.setAdmin(false);
        String name = NameUtil.getName();
        registerSaveLoginParam.setNick_name(name);
        registerSaveLoginParam.setOpen_id(openId);
        registerSaveLoginParam.setName(name);
        registerSaveLoginParam.setPhone(phone);

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

    private void initCustomerAssociation(Long id, String password) {
        CustomerWallet customerWallet = new CustomerWallet();
        customerWallet.setId(id);
        customerWallet.setAmount(new BigDecimal("0"));
        customerWallet.setPassword(password);
        customerWallet.setDeleted(false);
        customerWallet.setVersion(0);
        customerWallet.setCreateTime(new Date());
        iCustomerWalletService.save(customerWallet);

        CustomerSummary customerSummary = new CustomerSummary();
        customerSummary.setId(id);
        customerSummary.setTotalOrders(0);
        customerSummary.setTotalFine(new BigDecimal("0"));
        customerSummary.setVoucherNumbers(0);
        customerSummary.setCancelOrders(0);
        iCustomerSummaryService.save(customerSummary);

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
     * @param customer
     */
    private void saveCustomer(Customer customer,String openId) {
        customer.setSex(Constants.SEX_MAN);
        customer.setPhone("");
        customer.setCreateTime(new Date());
        customer.setVersion(0);
        customer.setDeleted(false);
        customer.setOpenId(openId);
        customer.setBitState(BitStatesUtil.OP_PHONE);
        customer.setName(NameUtil.getName());
        customer.setLevel(Constants.Level.LEVEL_BRONZE);
        super.save(customer);
    }
}
