package io.jopen.web.config.web;

import io.maxfeng.framework.core.security.access.APISignInterceptor;
import io.maxfeng.framework.core.security.access.RateLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 作者：MaXFeng
 * 时间：#{DATE}
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean("accessInterceptor")
    public APISignInterceptor accessInterceptor() {
        return new APISignInterceptor();
    }

    @Bean("rateLimitInterceptor")
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 流量限制拦截器  放在前面
         */
//        registry.addInterceptor(rateLimitInterceptor()).addPathPatterns("/**");
        /**
         * 接口加密访问验证拦截器  排在后面   架构的合理性
         */
//        registry.addInterceptor(accessInterceptor()).addPathPatterns("/**");
    }

}
