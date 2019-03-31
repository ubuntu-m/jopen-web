package io.jopen.web.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：限流注解
 * 作者：MaXFeng
 * 时间：2018/9/30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流唯一标示  在redis中表示K
     *
     * @return
     */
    String key() default "";

    /**
     * 限流时间
     *
     * @return
     */
    int time() default 10;

    /**
     * 限流次数
     * 10(time)可以访问16次
     *
     * @return
     */
    int count() default 16;

}
