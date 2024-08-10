package cn.itaka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("sa-token") // 从yaml中读取
public class SaTokenSettingProperties {

    private String permissionKey; // permission-key:%s

}
