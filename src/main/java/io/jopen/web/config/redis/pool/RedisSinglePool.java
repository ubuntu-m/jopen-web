package io.jopen.web.config.redis.pool;

import io.jopen.web.config.redis.RedisPool;
import io.jopen.web.core.context.ProjectHolder;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

/**
 * @author MaxFeng
 * @date 2018-7-9
 * @desc Redis单机模式连接池
 */
public class RedisSinglePool implements RedisPool {


    private static Environment env;

    /**
     * jedis连接池
     */
    private static JedisPool pool;
    /**
     * 最大连接数
     */
    private static Integer maxTotal;
    /**
     * 最大空闲连接数，在jedispool中最大的idle状态(空闲的)的jedis实例的个数
     */
    private static Integer maxIdle;
    /**
     * 最小空闲连接数，在jedispool中最小的idle状态(空闲的)的jedis实例的个数
     */
    private static Integer minIdle;
    /**
     * 在取连接时测试连接的可用性，在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
     */
    private static Boolean testOnBorrow;
    /**
     * 再还连接时不测试连接的可用性，在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
     */
    private static Boolean testOnReturn;
    /**
     * redis服务端ip
     */
    private static String redisIp;
    /**
     * redis服务端port
     */
    private static Integer redisPort;
    /**
     * redis连接超时时间
     */
    private static Integer redisTimeout;

    static {
        init();
    }

    private static void init() {
        env = ProjectHolder.getBean(Environment.class);
        maxTotal = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.pool.max-total")));

        maxIdle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.pool.max-idle")));

        minIdle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.pool.min-idle")));

        testOnBorrow = Boolean.parseBoolean(env.getProperty("spring.redis.pool.borrow"));

        testOnReturn = Boolean.parseBoolean(env.getProperty("spring.redis.pool.return"));

        redisTimeout = Integer.valueOf(Objects.requireNonNull(env.getProperty("spring.redis.timeout")));

        redisIp = env.getProperty("spring.redis.host");
        if (redisIp == null) {
            throw new RuntimeException("请检查redis服务端ip配置项redis.ip是否配置");
        }
        redisPort = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.port ")));

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        /**连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true*/
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, redisTimeout);
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void close(Jedis jedis) {
        jedis.close();
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

}
