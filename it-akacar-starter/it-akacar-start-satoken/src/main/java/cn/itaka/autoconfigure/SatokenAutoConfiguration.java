package cn.itaka.autoconfigure;

import cn.itaka.config.SatokenConfig;
import cn.itaka.config.StpInterfaceImpl;
import cn.itaka.intercepter.FeignTokenInterceptor;
import cn.itaka.properties.SaTokenSettingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@EnableConfigurationProperties({SaTokenSettingProperties.class})
public class SatokenAutoConfiguration {

    @Bean
    public SatokenConfig satokenConfig() {
        return new SatokenConfig();
    }

    @Bean
    public StpInterfaceImpl stpInterface(RedisTemplate redisTemplate,SaTokenSettingProperties saTokenSettingProperties) {
        return new StpInterfaceImpl(redisTemplate, saTokenSettingProperties);
    }

    @Bean
    public FeignTokenInterceptor feignTokenInterceptor() {
        return new FeignTokenInterceptor();
    }


}
