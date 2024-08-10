package cn.itaka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("cos")
public class CosSetingProperties {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
    private String detectType;
    private String imagePathTemplate;
}
