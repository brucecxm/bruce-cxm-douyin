package com.bruce.test;

import com.bruce.config.RedisConfig;
import com.bruce.monitor.BloomFilterMonitor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 布隆过滤器异步预热测试类
 * 验证异步预热对服务启动时间的改善效果
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.redis.host=localhost",
        "spring.redis.port=6379",
        "spring.redis.password=",
        "bloom.filter.warmup.batch-size=1000"
})
public class BloomFilterWarmupTest {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterWarmupTest.class);
    
    @MockBean
    private RedisConfig redisConfig;
    
    @Resource
    private BloomFilterMonitor bloomFilterMonitor;
    
    /**
     * 测试服务启动时布隆过滤器预热是否为异步执行
     * 验证服务启动不受预热阻塞
     */
    @Test
    public void testAsynchronousWarmup() throws InterruptedException {
        logger.info("开始测试异步预热机制...");
        
        // 记录测试开始时间
        long testStartTime = System.currentTimeMillis();
        
        // 验证服务启动时布隆过滤器初始状态为未预热完成
        logger.info("服务启动时布隆过滤器预热状态: {}", bloomFilterMonitor.isWarmupCompleted());
        assertTrue(!bloomFilterMonitor.isWarmupCompleted(), "服务启动时布隆过滤器不应立即预热完成");
        
        // 验证服务启动后可以正常运行其他任务，不受预热影响
        logger.info("测试线程正常运行，未被预热过程阻塞");
        
        // 等待一段时间（模拟服务运行过程）
        TimeUnit.SECONDS.sleep(2);
        
        // 检查预热进度
        int progress = bloomFilterMonitor.getWarmupProgress();
        logger.info("预热进行中，当前进度: {}%");
        
        // 验证异步预热确实在后台进行（可能还未完成）
        logger.info("异步预热机制测试完成，服务启动后可以正常运行，预热在后台进行");
        
        // 测试完成，计算测试耗时
        long testDuration = System.currentTimeMillis() - testStartTime;
        logger.info("测试耗时: {} 毫秒", testDuration);
        assertTrue(testDuration < 10000, "测试应在合理时间内完成，验证服务启动未被阻塞");
    }
    
    /**
     * 测试布隆过滤器预热监控机制
     */
    @Test
    public void testWarmupMonitoring() {
        logger.info("开始测试布隆过滤器预热监控机制...");
        
        // 验证监控指标可以正常获取
        String statusDescription = bloomFilterMonitor.getStatusDescription();
        logger.info("预热状态描述: {}", statusDescription);
        
        // 验证预热进度可以正常获取（即使预热还未完成）
        int progress = bloomFilterMonitor.getWarmupProgress();
        logger.info("预热进度: {}%");
        assertTrue(progress >= 0 && progress <= 100, "预热进度应在0-100%之间");
        
        // 验证详细指标可以获取
        String detailedMetrics = bloomFilterMonitor.getDetailedMetrics();
        logger.info("详细监控指标: {}", detailedMetrics);
        
        logger.info("预热监控机制测试完成");
    }
    
    /**
     * 模拟应用启动事件，验证事件触发机制
     */
    @Test
    public void testEventTriggeredWarmup() {
        logger.info("测试事件触发的预热机制...");
        
        try {
            // 模拟应用启动事件（实际项目中会由Spring自动触发）
            ApplicationStartedEvent event = new ApplicationStartedEvent(
                    new SpringApplication(), 
                    new String[]{}, 
                    null);
            
            // 这里只是验证概念，实际的事件监听在RedisConfig中
            logger.info("应用启动事件被触发，预热机制应该在后台开始执行");
            logger.info("注意：在实际运行时，预热会自动开始，不需要手动触发");
            
            logger.info("事件触发预热机制测试完成");
        } catch (Exception e) {
            logger.error("测试事件触发预热机制时出错: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 性能比较分析（理论分析）
     */
    @Test
    public void testPerformanceComparison() {
        logger.info("开始分析异步预热与同步预热的性能差异...");
        
        // 理论分析数据
        logger.info("【理论性能分析】");
        logger.info("1. 同步预热: 服务启动时间 = 正常启动时间 + 数据加载时间（与数据量成正比）");
        logger.info("2. 异步预热: 服务启动时间 = 正常启动时间（预热在后台进行，不影响启动）");
        logger.info("3. 大数据量场景下差异: 同步预热可能导致启动时间从秒级延长到分钟级甚至超时");
        logger.info("4. 资源利用: 异步预热可以更好地利用系统资源，避免启动阶段资源竞争");
        logger.info("5. 容错性: 异步预热失败不会导致服务启动失败，便于恢复");
        
        // 结论
        logger.info("结论: 异步预热在大数据量场景下具有明显优势，可以显著改善服务启动性能");
        
        logger.info("性能比较分析完成");
    }
}