package io.jopen.web.config.redis;

import io.jopen.web.config.redis.pool.RedisSinglePool;
import redis.clients.jedis.Jedis;

/**
 * @author snowalker
 * @date 2018-7-9
 * @desc 封装单机版Jedis工具类
 */
public class RedisPoolUtil {

    private RedisPoolUtil(){}

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisSinglePool.getJedis();
            result = jedis.get(key);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }

    public static Long setnx(String key, String value){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisSinglePool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }

    public static String getSet(String key, String value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisSinglePool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }

    public static Long expire(String key, int seconds){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisSinglePool.getJedis();
            result = jedis.expire(key, seconds);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisSinglePool.getJedis();
            result = jedis.del(key);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }
}
