package cn.itaka.feign;

import cn.itaka.constants.Constants;
import cn.itaka.fallback.AppLoginFeignClientFallbackFactory;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = Constants.Remote.SERVICE_UAA, path = "/app/login", contextId = "AppLoginFeignClient", fallbackFactory =  AppLoginFeignClientFallbackFactory.class)
public interface AppLoginFeignClient {
    @PostMapping
    R<Void> save(@RequestBody RegisterSaveLoginParam registerSaveLoginParam);
}
