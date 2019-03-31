package io.jopen.web.config.redis.lock;

import io.jopen.web.config.redis.RedisPoolUtil;
import io.jopen.web.core.context.ProjectHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * 描述：利用Jedis实现分布式锁
 * 作者：MaXFeng
 * 时间：2018/10/5
 */
public class DefaultDistributeLock implements DistributedLock {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 加锁默认十秒钟失效
     */
    private static final int DEFAULT_LOCK_EXPIRE = 10;

    /**
     * 不提供外界访问
     */
    private DefaultDistributeLock() {

    }

    private static volatile DefaultDistributeLock defaultDistributeLock;


    public static DefaultDistributeLock getInstance() {
        if (defaultDistributeLock != null) {
            return defaultDistributeLock;
        } else {
            /**
             * 加锁对象为当前类
             */
            synchronized (DefaultDistributeLock.class) {
                return new DefaultDistributeLock();
            }
        }
    }

    @Override
    public boolean lock(String lockName) {

        Environment env = ProjectHolder.getBean(Environment.class);
        /**1.使用setNx开始加锁*/
        log.info("开始获取Redis分布式锁流程,lockName={},CurrentThreadName={}", lockName, Thread.currentThread().getName());
        long lockTimeout = Long.parseLong(Objects.requireNonNull(env.getProperty("spring.redis.timeout")));
        /**redis中锁的值为:当前时间+超时时间*/
        Long lockResult = RedisPoolUtil.setnx(lockName, String.valueOf(System.currentTimeMillis() + lockTimeout));

        if (lockResult != null && lockResult.intValue() == 1) {
            log.info("setNx获取分布式锁[成功],threadName={}", Thread.currentThread().getName());
            RedisPoolUtil.expire(lockName, DEFAULT_LOCK_EXPIRE);
            return true;
        } else {
            log.info("setNx获取分布式锁[失败],threadName={}", Thread.currentThread().getName());
            return false;
        }
    }

    @Override
    public boolean release(String lockName) {
        Long result = RedisPoolUtil.del(lockName);
        if (result != null && result.intValue() == 1) {
            log.info("删除Redis分布式锁成功，锁已释放, key= :{}", lockName);
            return true;
        }
        log.info("删除Redis分布式锁失败，锁未释放, key= :{}", lockName);
        return false;
    }


}
