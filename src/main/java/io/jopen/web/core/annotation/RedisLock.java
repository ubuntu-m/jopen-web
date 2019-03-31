package io.jopen.web.core.annotation;

import java.lang.annotation.*;

/**
 * @author MaXuefeng
 * @date 2018-7-9
 * @desc redisLock自定义注解
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RedisLock {

    String value() default "redis-lock";
}
