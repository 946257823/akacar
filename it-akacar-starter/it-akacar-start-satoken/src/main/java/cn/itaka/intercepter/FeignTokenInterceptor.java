package cn.itaka.intercepter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
// 不能加component 因为是在start里面
public class FeignTokenInterceptor implements RequestInterceptor {
    /**
     * 每次发送feign请求之前都会进入此方法，帮我们从上一次的请求头中把token拿到，携带到下一次的请求头中
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String tokenName = StpUtil.getTokenName();
        String tokenValue = StpUtil.getTokenValue();
        if (StrUtil.isNotEmpty(tokenName) && StrUtil.isNotEmpty(tokenValue)) {
            requestTemplate.header(tokenName,tokenValue);
        }
    }
}
