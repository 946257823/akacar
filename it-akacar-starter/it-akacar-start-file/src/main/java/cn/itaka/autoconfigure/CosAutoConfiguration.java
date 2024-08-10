package cn.itaka.autoconfigure;


import cn.itaka.properties.CosSetingProperties;
import cn.itaka.template.CosTemplate;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CosSetingProperties.class})
public class CosAutoConfiguration {

    /**
     * 利用bean在springboot启动的时候加载一次的机制，加载cosClient
     * @param cosSetingProperties
     * @return
     */
    @Bean
    public CosTemplate cosTemplate(CosSetingProperties cosSetingProperties) {
        COSClient cosClient = this.initCosClient(cosSetingProperties);
        return new CosTemplate(cosSetingProperties, cosClient);
    }

    public COSClient initCosClient(CosSetingProperties cosSetingProperties) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        COSCredentials cred = new BasicCOSCredentials(cosSetingProperties.getSecretId(), cosSetingProperties.getSecretKey());
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(cosSetingProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

}
