package com.bruce.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 提供Redis的序列化配置、Redisson客户端配置、布隆过滤器初始化
 */
@Configuration
@EnableCaching  // 启用缓存
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host:localhost}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    /**
     * 配置RedisTemplate
     * 使用Jackson2JsonRedisSerializer进行序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // 键采用String序列化
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 配置StringRedisTemplate
     * 用于操作字符串类型的数据
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * 配置Redisson客户端
     * 用于分布式锁、布隆过滤器等高级功能
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单节点Redis配置（集群/哨兵模式可按需修改）
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(0);  // 可根据需求指定Redis数据库
        return Redisson.create(config);
    }

    /**
     * 初始化用户ID布隆过滤器
     * 用于缓存穿透防护，提前过滤不存在的用户ID
     */
    @Bean
    public RBloomFilter<Long> userBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter("userBloomFilter");
        // 初始化参数：预计元素数量100万，误判率0.01（可根据业务调整）
        bloomFilter.tryInit(1000000L, 0.01);

        // 【可选】若需预加载用户ID到布隆过滤器，可在此处调用业务逻辑
        // 例如：userService.listAllUserId().forEach(id -> bloomFilter.add(id));

        return bloomFilter;
    }
}
