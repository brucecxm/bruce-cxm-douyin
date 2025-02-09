package com.bruce.config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonConfig {
    public static RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379"); // 替换成你的Redis地址
        return Redisson.create(config);
    }
}
