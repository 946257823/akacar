package cn.itaka.fallback;


import cn.itaka.feign.AppLoginFeignClient;
import cn.itaka.pojo.param.RegisterSaveLoginParam;
import cn.itaka.result.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AppLoginFeignClientFallbackFactory implements FallbackFactory<AppLoginFeignClient> {
    @Override
    public AppLoginFeignClient create(Throwable cause) {
        cause.printStackTrace(); // 一定要打印异常
        return new AppLoginFeignClient() {
            @Override
            public R<Void> save(RegisterSaveLoginParam registerSaveLoginParam) {
                return R.error();
            }
        };
    }
}