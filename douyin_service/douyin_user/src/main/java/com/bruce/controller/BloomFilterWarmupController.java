package com.bruce.controller;

import com.bruce.monitor.BloomFilterMonitor;
import com.bruce.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 布隆过滤器预热监控控制器
 * 用于测试和验证异步预热对服务启动时间的影响
 */
@RestController
@RequestMapping("/bloom-filter")
public class BloomFilterWarmupController {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterWarmupController.class);
    
    @Autowired
    private BloomFilterMonitor bloomFilterMonitor;
    
    // 服务启动时间（毫秒时间戳）
    private final AtomicLong serviceStartTime = new AtomicLong(0);
    
    // 服务完全就绪时间（毫秒时间戳）
    private final AtomicLong serviceReadyTime = new AtomicLong(0);
    
    @PostConstruct
    public void init() {
        // 记录服务启动时间
        serviceStartTime.set(System.currentTimeMillis());
        serviceReadyTime.set(System.currentTimeMillis());
        logger.info("服务启动完成，耗时: {} 毫秒，当前时间: {}", 
                serviceReadyTime.get() - serviceStartTime.get(), 
                LocalDateTime.now());
    }
    
    /**
     * 获取布隆过滤器预热状态和服务启动指标
     */
    @GetMapping("/warmup/status")
    public Result getWarmupStatus() {
        Map<String, Object> result = new HashMap<>();
        
        // 服务启动相关指标
        result.put("serviceStartTime", serviceStartTime.get());
        result.put("serviceReadyTime", serviceReadyTime.get());
        result.put("serviceStartupDurationMs", serviceReadyTime.get() - serviceStartTime.get());
        result.put("serviceStartupTime", 
                LocalDateTime.ofEpochSecond(serviceStartTime.get() / 1000, 0, ZoneOffset.UTC));
        result.put("currentTime", LocalDateTime.now());
        
        // 布隆过滤器预热状态
        result.put("warmupCompleted", bloomFilterMonitor.isWarmupCompleted());
        result.put("warmupProgress", bloomFilterMonitor.getWarmupProgress());
        result.put("warmupDuration", bloomFilterMonitor.getWarmupDuration());
        
        // 详细统计信息
        result.put("statistics", bloomFilterMonitor.getStatistics());
        result.put("statusDescription", bloomFilterMonitor.getStatusDescription());
        
        logger.debug("布隆过滤器预热状态查询: 预热完成={}, 进度={}%, 服务已运行={}秒",
                bloomFilterMonitor.isWarmupCompleted(),
                bloomFilterMonitor.getWarmupProgress(),
                (System.currentTimeMillis() - serviceStartTime.get()) / 1000);
        
        return Result.success(result);
    }
    
    /**
     * 获取详细的监控指标
     */
    @GetMapping("/warmup/metrics")
    public Result getDetailedMetrics() {
        String metrics = bloomFilterMonitor.getDetailedMetrics();
        logger.info("获取布隆过滤器详细监控指标");
        return Result.success(metrics);
    }
    
    /**
     * 测试布隆过滤器性能（可选）
     */
    @GetMapping("/performance/test")
    public Result testPerformance() {
        long startTime = System.currentTimeMillis();
        // 这里可以添加性能测试逻辑
        long duration = System.currentTimeMillis() - startTime;
        
        Map<String, Object> result = new HashMap<>();
        result.put("testDurationMs", duration);
        result.put("warmupCompleted", bloomFilterMonitor.isWarmupCompleted());
        result.put("serviceUptimeMs", System.currentTimeMillis() - serviceStartTime.get());
        
        return Result.success(result);
    }
    
    /**
     * 比较异步预热与同步预热的效果（理论对比）
     */
    @GetMapping("/comparison")
    public Result getComparison() {
        Map<String, Object> comparison = new HashMap<>();
        
        // 异步预热优势
        Map<String, String> asyncAdvantages = new HashMap<>();
        asyncAdvantages.put("serviceStartup", "服务启动不受预热影响，可立即对外提供服务");
        asyncAdvantages.put("userExperience", "即使在预热完成前，通过业务代码兼容也能正常处理请求");
        asyncAdvantages.put("resourceUtilization", "预热过程在后台线程执行，不会阻塞主线程和其他服务启动");
        asyncAdvantages.put("scalability", "支持分批处理，可根据系统负载动态调整预热速度");
        asyncAdvantages.put("monitoring", "提供详细的预热进度和性能指标监控");
        
        // 同步预热劣势
        Map<String, String> syncDisadvantages = new HashMap<>();
        syncDisadvantages.put("serviceStartup", "服务启动时间与预热数据量成正比，大量数据会导致启动缓慢甚至超时");
        syncDisadvantages.put("resourceBlock", "预热过程会阻塞主线程，占用系统资源");
        syncDisadvantages.put("failoverImpact", "预热失败会直接影响服务启动");
        syncDisadvantages.put("scalability", "数据量增长时，启动时间线性增加，难以应对大数据量场景");
        
        comparison.put("asyncWarmupAdvantages", asyncAdvantages);
        comparison.put("syncWarmupDisadvantages", syncDisadvantages);
        comparison.put("recommendation", "对于大数据量场景，强烈推荐使用异步预热方案");
        
        return Result.success(comparison);
    }
}