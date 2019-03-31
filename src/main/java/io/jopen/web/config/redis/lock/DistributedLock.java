package io.jopen.web.config.redis.lock;

import io.jopen.web.config.redis.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maxfeng
 * @date 2018-7-9
 * @desc Redis分布式锁接口声明
 */
public interface DistributedLock {

    Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

    /**
     * 加锁
     *
     * @param lockName
     * @return
     */
    boolean lock(String lockName);

    /**
     * 解锁
     *
     * @param lockName
     */
    boolean release(String lockName);

    default boolean tryLock(String lockName, long lockTimeout, int EXPIRE_SECONDS) {
/**
 * 2.加锁失败后再次尝试
 * 2.1获取锁失败，继续判断，判断时间戳，看是否可以重置并获取到锁
 *    setNx结果小于当前时间，表明锁已过期，可以再次尝试加锁
 */
        String lockValueStr = RedisPoolUtil.get(lockName);
        Long lockValueATime = Long.parseLong(lockValueStr);
        LOGGER.info("lockValueATime为:" + lockValueATime);
        if (lockValueStr != null && lockValueATime < System.currentTimeMillis()) {

            /**2.2再次用当前时间戳getset--->将给定 key 的值设为 value，并返回 key 的旧值(old value)
             * 通过getset重设锁对应的值: 新的当前时间+超时时间，并返回旧的锁对应值
             */
            String getSetResult = RedisPoolUtil.getSet(lockName, String.valueOf(System.currentTimeMillis() + lockTimeout));
            LOGGER.info("lockValueBTime为:" + Long.parseLong(getSetResult));
            if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                /**
                 *2.3旧值判断，是否可以获取锁
                 *当key没有旧值时，即key不存在时，返回nil ->获取锁，设置锁过期时间
                 */
                LOGGER.info("获取Redis分布式锁[成功],lockName={},CurrentThreadName={}",
                        lockName, Thread.currentThread().getName());
                RedisPoolUtil.expire(lockName, EXPIRE_SECONDS);
                return true;
            } else {
                LOGGER.info("获取锁失败,lockName={},CurrentThreadName={}",
                        lockName, Thread.currentThread().getName());
                return false;
            }
        } else {
            /**3.锁未超时，获取锁失败*/
            LOGGER.info("当前锁未失效！！！！，竞争失败，继续持有之前的锁,lockName={},CurrentThreadName={}",
                    lockName, Thread.currentThread().getName());
            return false;
        }
    }
}
