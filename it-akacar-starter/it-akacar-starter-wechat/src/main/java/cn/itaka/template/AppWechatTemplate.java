package cn.itaka.template;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.itaka.autoconfigure.WechatAutoConfiguration;
import cn.itaka.properties.AppSettingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
public class AppWechatTemplate {

    private AppSettingProperties appSettingProperties;

   public AppWechatTemplate(AppSettingProperties appSettingProperties) {
       this.appSettingProperties = appSettingProperties;
   }

    // 获取access_token
    public String getAccessToken() {
        // 1.获取access_token
        String accessTokenUrl = String.format(appSettingProperties.getAccessTokenUrl(), appSettingProperties.getAppId(), appSettingProperties.getSecret());
        String resultJsonStr = HttpUtil.get(accessTokenUrl);
//        JSONObject accessTokenJson = JSONUtil.parseObj(resultJsonStr);
        // 2.把字符串json转成jsonObj对象，方便获取值
        JSONObject accessTokenResult = JSONUtil.toBean(resultJsonStr, JSONObject.class);
        String accessToken = accessTokenResult.getStr("access_token");
        if (StrUtil.isEmpty(accessToken)) {
            throw new RuntimeException("获取access_token失败");
        }
        return accessToken;
    }

    /**
     * 获取小程序用户手机号
     * @param phoneCode
     * @return
     */
    public String getPhoneByAccessToken(String phoneCode) {
//        String accessToken = this.getAccessToken();
//        // 2.获取手机号
//        String phoneUrl = String.format(appSettingProperties.getPhoneUrl(), accessToken);
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("code", phoneCode);
//        String resultPhoneStr = HttpUtil.post(phoneUrl, body);
//
//        JSONObject resultJsonObj = JSONUtil.parseObj(resultPhoneStr);
//        Integer errcode = resultJsonObj.getInt("errcode");
//        if (0 != errcode) {
//            throw new RuntimeException("获取手机号失败");
//        }
//
//        JSONObject phoneInfoObj = resultJsonObj.getJSONObject("phone_info");
//        String phoneNumber = phoneInfoObj.getStr("phoneNumber");
//        if (StrUtil.isEmpty(phoneNumber)) {
//            throw new RuntimeException("获取手机号失败");
//        }
        return "12345678911";
    }

    public String getOpenIdByCode(String openIdCode) {
        // 1. 先根据openIdCode拿到OpenId
        String urltemplate = appSettingProperties.getOpenidUrl();
        String url = String.format(urltemplate,appSettingProperties.getAppId(),appSettingProperties.getSecret(),openIdCode);
        // 获取到返回参数,类型为Json字符串
        String jsonResultStr = HttpUtil.get(url);
        // 将返回的json字符串转为JSONOBJ对象
        if (StrUtil.isEmpty(jsonResultStr)) {
            throw new RuntimeException("获取openid失败");
        }
        JSONObject resultJsonObject = JSONUtil.parseObj(jsonResultStr);
        String openid = resultJsonObject.getStr("openid");
        if (StrUtil.isEmpty(openid)) {
            throw new RuntimeException("获取openid失败");
        }
        return openid;
    }
}
