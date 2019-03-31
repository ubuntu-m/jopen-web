package io.jopen.web.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.Serializable;

/**
 * 描述： Redisson提供了面向业务的限流器
 * 此处不采用redisson的限流器是因为限流的公平性不能保证
 * （redisson采用的算法是令牌桶算法，并发是存在问题的）
 * <p>
 * 作者：MaXFeng
 * 时间：2018/01/30
 */
@Configuration
public class RateLimitConfig {


    /**
     * 加载Lua限流脚本
     *
     * @return
     */
    @Bean
    public DefaultRedisScript<Number> redisScript() {
        DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Number.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimit.lua")));
        return redisScript;
    }

    /**
     * RedisTemplate
     * redis客户端
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
