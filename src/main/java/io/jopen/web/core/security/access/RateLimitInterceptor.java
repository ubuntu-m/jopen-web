package io.jopen.web.core.security.access;

import io.maxfeng.framework.core.annotation.RateLimit;
import io.maxfeng.framework.core.model.ErrorEnum;
import io.maxfeng.framework.core.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static io.maxfeng.framework.core.model.ResponseModel.error;

/**
 * 描述： 对接口访问增加流量控制
 * 作者：MaXFeng
 * 时间：2018/9/30
 */
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private DefaultRedisScript<Number> defaultRedisScript;

    /**
     * @param request
     * @param response
     * @param handler  Class：HandlerMethod
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        /**
         * 必须先解析方法才能获取到方法级别的注解
         */
        RateLimit annotation = handlerMethod.getResolvedFromHandlerMethod().getMethodAnnotation(RateLimit.class);
        if (annotation != null) {
            String ipAddr = WebUtils.getIpAddr(request);
            /**
             * 采用线程安全类
             */
            StringBuffer k = new StringBuffer();
            k.append(ipAddr).append("-").append(method.getName()).append("-").append(annotation.key());
            List<String> keys = Collections.singletonList(k.toString());
            Number r = redisTemplate.execute(defaultRedisScript, keys, annotation.count(), annotation.time());
            if (r != null && r.intValue() != 0 && r.intValue() <= annotation.count()) {
                return true;
            } else {
                WebUtils.write4Response(response, error(ErrorEnum.FREQUENT_REQUESTS));
                return false;
            }
        }
        return true;
    }
}
