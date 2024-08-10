package cn.itaka.autoconfigure;

import cn.itaka.properties.AppSettingProperties;
import cn.itaka.template.AppWechatTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppSettingProperties.class})
public class WechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(
            name = {"wechatTemplate"} // bean里面没有才会创建，避免重复创建
    )
    public AppWechatTemplate wechatTemplate(AppSettingProperties appSettingProperties) {
        AppWechatTemplate appWechatTemplate = new AppWechatTemplate(appSettingProperties);
        return appWechatTemplate;
    }
}
