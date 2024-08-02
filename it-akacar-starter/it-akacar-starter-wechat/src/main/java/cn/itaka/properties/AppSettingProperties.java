package cn.itaka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("wechat.app")
public class AppSettingProperties {

    private String appId;

    private String secret;

    private String openidUrl;

    private String accessTokenUrl;

    private String phoneUrl;
}
