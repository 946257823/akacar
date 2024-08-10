package cn.itaka.exception;



import cn.hutool.core.util.StrUtil;
import cn.itaka.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
@RestControllerAdvice  //全局异常捕获
@Slf4j
public class GlobalExceptionHandler {
    // 捕捉Exception异常，返回统一的错误提示
    @ExceptionHandler(value = Exception.class)
    public R exceptionErrorMethod(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        log.error("发生异常错误，请求地址：{}，异常信息{}", request.getRequestURI(), e.getMessage());

//        if (StrUtil.isNotBlank(RootContext.getXID())) {
//            response.setSatus(500);
//        }

        if ("NotLoginException".equals(e.getClass().getSimpleName())) {
            return R.error(401, "请登录后再访问");
        }

        if ("NotPermissionException".equals(e.getClass().getSimpleName())) {
            return R.error(401, "没有登录权限");
        }

        return R.error(500, "服务器内部异常，请联系管理员");
    }
    // 捕捉GlobalException异常，把错误日志抛给用户 
    @ExceptionHandler(value = GlobalException.class)
    public R globalCustomMethod(GlobalException e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("发生异常错误，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return R.error(e.getCode(), e.getMsg());
    }
}

