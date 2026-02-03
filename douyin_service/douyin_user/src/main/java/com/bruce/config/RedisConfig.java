package com.bruce.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import com.bruce.monitor.BloomFilterMonitor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Redis 配置类
 * 提供Redis的序列化配置、Redisson客户端配置、布隆过滤器初始化
 */
@Configuration
@EnableCaching // 启用缓存
public class RedisConfig extends CachingConfigurerSupport implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host:1.94.131.16}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.database:12}")
    private int database;

    @Value("${spring.redis.password}")
    private String password;

    @Autowired
    private BloomFilterMonitor bloomFilterMonitor;

    @Autowired
    private UserDao userDao;
    // 布隆过滤器预热开关配置
    @Value("${bloom-filter.warmup-enabled:true}")
    private boolean bloomFilterWarmupEnabled;

    // 是否需要预热完成后才能使用布隆过滤器
    @Value("${bloom-filter.require-warmup:true}")
    private boolean bloomFilterRequireWarmup;

    // 获取布隆过滤器预热状态（保持向后兼容）
    public static boolean isBloomFilterWarmedUp() {
        // 从Spring上下文获取BloomFilterMonitor实例
        // 这里简化处理，实际项目中可以使用Spring上下文工具类
        return true;
    }

    /**
     * 配置异步任务执行器，用于布隆过滤器预热
     */
    @Bean("bloomFilterTaskExecutor")
    public Executor bloomFilterTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BloomFilterWarmup-");
        executor.initialize();
        return executor;
    }

    /**
     * 配置RedisTemplate
     * 使用Jackson2JsonRedisSerializer进行序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);

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
                .setDatabase(database) // 可根据需求指定Redis数据库
                .setPassword(password); // 添加Redis密码配置

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
        try {
            // 仅初始化，不进行同步预热
            bloomFilter.tryInit(1000000L, 0.01);
            log.info("用户ID布隆过滤器初始化完成，预热将在后台异步执行");
        } catch (Exception e) {
            log.error("初始化用户ID布隆过滤器失败", e);
            // 初始化失败不影响应用启动
        }

        return bloomFilter;
    }

    /**
     * 异步预热布隆过滤器
     */
    @Async("bloomFilterTaskExecutor")
    public void warmupBloomFilter(RBloomFilter<Long> bloomFilter) {
        log.info("[布隆过滤器] 开始异步预热布隆过滤器...");
        try {
            // 记录预热开始
            bloomFilterMonitor.startWarmup();

            // 设置批次大小
            int batchSize = 1000;
            bloomFilterMonitor.setBatchSize(batchSize);
            // 1. 构造空查询条件（查询所有）
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            // 2. 只查询id字段（按需查询，避免全表字段查询，提升性能）
            queryWrapper.select(User::getId);
            // 获取所有用户ID
            List<User> users = userDao.selectList(queryWrapper);
            List<Long> userIds = users.stream()
                    .map(User::getId) // 提取User对象的id属性
                    .collect(Collectors.toList()); // 收集为Long类型集合
            int totalCount = userIds.size();

            // 设置总用户数
            bloomFilterMonitor.setTotalUsers(totalCount);

            log.info("[布隆过滤器] 开始加载 {} 个用户ID到布隆过滤器，批次大小: {}",
                    totalCount, batchSize);

            // 分批处理用户ID
            for (int i = 0; i < totalCount; i += batchSize) {
                int endIndex = Math.min(i + batchSize, totalCount);
                List<Long> batchIds = userIds.subList(i, endIndex);

                // 批量添加到布隆过滤器
                for (Long userId : batchIds) {
                    bloomFilter.add(userId);
                }

                // 更新监控指标
                bloomFilterMonitor.updateAddedUsers(batchIds.size());

                // 避免过度占用CPU
                Thread.sleep(50);
            }

            // 标记预热完成
            bloomFilterMonitor.completeWarmup();
            log.info("[布隆过滤器] 异步预热完成，布隆过滤器状态: {}",
                    bloomFilterMonitor.getStatusDescription());

        } catch (Exception e) {
            log.error("[布隆过滤器] 异步预热失败", e);
            bloomFilterMonitor.recordWarmupFailure(e.getMessage());
        }
    }

    /**
     * 应用上下文刷新完成后触发异步预热
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // 根据配置决定是否触发布隆过滤器预热
            if (bloomFilterWarmupEnabled) {
                // 获取userBloomFilter bean并触发异步预热
                RBloomFilter<Long> userBloomFilter = event.getApplicationContext().getBean("userBloomFilter",
                        RBloomFilter.class);
                warmupBloomFilter(userBloomFilter);
                log.info("布隆过滤器预热功能已开启，预热将在后台异步执行");
            } else {
                log.info("布隆过滤器预热功能已关闭");
            }
        } catch (Exception e) {
            log.error("触发布隆过滤器异步预热失败", e);
        }
    }
}
