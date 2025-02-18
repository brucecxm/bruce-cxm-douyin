package com.bruce.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值并设置过期时间
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除一个key
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 增加数值
     */
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 设置哈希值
     */
    public void hSet(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取哈希值
     */
    public String hGet(String key, String hashKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 删除哈希字段
     */
    public void hDel(String key, String hashKey) {
        stringRedisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 获取指定前缀的所有key
     */
    public void keys(String pattern) {
        stringRedisTemplate.keys(pattern);
    }

    /**
     * 设置key的过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取key的剩余过期时间
     */
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }
}
