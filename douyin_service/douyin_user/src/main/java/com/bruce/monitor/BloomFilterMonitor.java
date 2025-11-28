package com.bruce.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 布隆过滤器预热监控器
 * 提供预热状态查询、进度统计和详细性能指标监控
 */
@Component
public class BloomFilterMonitor {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterMonitor.class);

    // 批次大小
    private final AtomicInteger batchSize = new AtomicInteger(1000);

    // 当前批次
    private final AtomicInteger currentBatch = new AtomicInteger(0);

    // 平均处理速率（用户/秒）
    private final AtomicLong avgProcessingRate = new AtomicLong(0);

    // 预计剩余时间（毫秒）
    private final AtomicLong estimatedRemainingTime = new AtomicLong(0);

    // 最后一次进度更新时间
    private long lastProgressUpdateTime = 0;

    // 上一次添加的用户数
    private int lastAddedUsers = 0;

    // 内存占用估算（字节）
    private final AtomicLong estimatedMemoryUsage = new AtomicLong(0);

    // 预热模式
    private final AtomicReference<String> warmupMode = new AtomicReference<>("ASYNC");

    // 布隆过滤器预热完成状态
    private final AtomicBoolean warmupCompleted = new AtomicBoolean(false);

    // 预热开始时间（毫秒）
    private final AtomicLong warmupStartTime = new AtomicLong(0);

    // 预热结束时间（毫秒）
    private final AtomicLong warmupEndTime = new AtomicLong(0);

    // 预热总用户数
    private final AtomicInteger totalUsers = new AtomicInteger(0);

    // 已添加到布隆过滤器的用户数
    private final AtomicInteger addedUsers = new AtomicInteger(0);

    // 预热失败次数
    private final AtomicInteger warmupFailures = new AtomicInteger(0);

    /**
     * 开始预热监控
     */
    public void startWarmup() {
        warmupStartTime.set(System.currentTimeMillis());
        this.lastProgressUpdateTime = warmupStartTime.get();
        warmupCompleted.set(false);
        addedUsers.set(0);
        this.lastAddedUsers = 0;
        this.currentBatch.set(0);
        this.avgProcessingRate.set(0);
        this.estimatedRemainingTime.set(0);
        logger.info("[布隆过滤器预热监控] 开始预热，模式: {}", warmupMode.get());
    }

    /**
     * 设置总用户数
     * 
     * @param total 总用户数
     */
    public void setTotalUsers(int total) {
        totalUsers.set(total);
        // 估算内存占用 (每个元素约需要 10 bits * 哈希函数数量)
        // 假设使用 7 个哈希函数
        this.estimatedMemoryUsage.set((long) (total * 7 * 1.25));
        logger.info("[布隆过滤器预热监控] 总用户数设置为: {}, 估算内存占用: {} KB",
                total, estimatedMemoryUsage.get() / 1024);
    }

    /**
     * 更新已添加用户数
     * 
     * @param count 新增用户数
     * @return 当前已添加用户数
     */
    public int updateAddedUsers(int count) {
        int current = addedUsers.addAndGet(count);
        this.currentBatch.incrementAndGet();

        // 更新进度指标
        updateProgressMetrics();

        // 每10个批次或完成时记录详细日志
        int currentBatchNum = currentBatch.get();
        if (currentBatchNum % 10 == 0 || current >= totalUsers.get()) {
            logger.info("[布隆过滤器预热监控] 批次: {}, 进度: {:.2f}%, 已添加: {}/{} 用户, " +
                    "速率: {} 用户/秒, 预计剩余: {} 秒",
                    currentBatchNum, getWarmupProgress(), current, totalUsers.get(),
                    avgProcessingRate.get(), estimatedRemainingTime.get() / 1000);
        } else {
            logger.debug("[布隆过滤器预热监控] 进度: {}/{} ({:.2f}%)",
                    current, totalUsers.get(), getWarmupProgress());
        }
        return current;
    }

    /**
     * 更新进度指标
     */
    private void updateProgressMetrics() {
        long currentTime = System.currentTimeMillis();
        int currentAdded = addedUsers.get();

        // 计算时间差和处理的用户数差
        long timeDiffMs = currentTime - lastProgressUpdateTime;
        int userDiff = currentAdded - lastAddedUsers;

        if (timeDiffMs > 1000 && userDiff > 0) {
            // 计算处理速率（用户/秒）
            long rate = (long) ((double) userDiff * 1000 / timeDiffMs);
            avgProcessingRate.set(rate);

            // 计算剩余用户数
            int remainingUsers = totalUsers.get() - currentAdded;
            if (remainingUsers > 0 && rate > 0) {
                // 估算剩余时间（毫秒）
                long remainingTimeMs = (long) ((double) remainingUsers * 1000 / rate);
                estimatedRemainingTime.set(remainingTimeMs);
            }

            // 更新最后更新时间和已添加用户数
            lastProgressUpdateTime = currentTime;
            lastAddedUsers = currentAdded;
        }
    }

    /**
     * 完成预热
     */
    public void completeWarmup() {
        warmupEndTime.set(System.currentTimeMillis());
        warmupCompleted.set(true);

        long durationMs = getWarmupDuration();
        double durationSec = durationMs / 1000.0;
        int total = addedUsers.get();
        double throughput = total / durationSec;

        logger.info("[布隆过滤器预热监控] 预热完成! 统计信息:");
        logger.info("[布隆过滤器预热监控] - 总耗时: {:.2f} 秒", durationSec);
        logger.info("[布隆过滤器预热监控] - 处理用户数: {}", total);
        logger.info("[布隆过滤器预热监控] - 平均吞吐量: {:.2f} 用户/秒", throughput);
        logger.info("[布隆过滤器预热监控] - 内存占用估算: {} KB", estimatedMemoryUsage.get() / 1024);
        logger.info("[布隆过滤器预热监控] - 总批次: {}", currentBatch.get());
    }

    /**
     * 记录预热失败
     */
    public void recordWarmupFailure() {
        int failures = warmupFailures.incrementAndGet();
        logger.error("[布隆过滤器预热监控] 预热失败，失败次数: {}, 已处理用户数: {}, 耗时: {} 毫秒",
                failures, addedUsers.get(), getWarmupDuration());
    }

    /**
     * 记录预热失败（带原因）
     * 
     * @param reason 失败原因
     */
    public void recordWarmupFailure(String reason) {
        int failures = warmupFailures.incrementAndGet();
        logger.error("[布隆过滤器预热监控] 预热失败，失败次数: {}, 原因: {}, 已处理用户数: {}, 耗时: {} 毫秒",
                failures, reason, addedUsers.get(), getWarmupDuration());
    }

    /**
     * 设置批次大小
     * 
     * @param size 批次大小
     */
    public void setBatchSize(int size) {
        this.batchSize.set(size);
        logger.debug("[布隆过滤器预热监控] 批次大小设置为: {}", size);
    }

    /**
     * 获取详细的监控指标
     * 
     * @return 详细监控指标字符串
     */
    public String getDetailedMetrics() {
        StringBuilder metrics = new StringBuilder();
        metrics.append("布隆过滤器预热监控指标:\n");
        metrics.append("- 预热状态: ").append(warmupCompleted.get() ? "已完成" : (warmupFailures.get() > 0 ? "已失败" : "进行中"))
                .append("\n");
        metrics.append("- 预热模式: ").append(warmupMode.get()).append("\n");
        metrics.append("- 总用户数: ").append(totalUsers.get()).append("\n");
        metrics.append("- 已添加用户数: ").append(addedUsers.get()).append("\n");
        metrics.append("- 进度百分比: ").append(String.format("%.2f", getWarmupProgress())).append("%\n");
        metrics.append("- 总耗时: ").append(getWarmupDuration()).append("ms\n");
        metrics.append("- 平均处理速率: ").append(avgProcessingRate.get()).append("用户/秒\n");
        metrics.append("- 预计剩余时间: ").append(estimatedRemainingTime.get() / 1000).append("秒\n");
        metrics.append("- 当前批次: ").append(currentBatch.get()).append("\n");
        metrics.append("- 批次大小: ").append(batchSize.get()).append("\n");
        metrics.append("- 内存占用估算: ").append(estimatedMemoryUsage.get() / 1024).append("KB\n");
        metrics.append("- 失败次数: ").append(warmupFailures.get()).append("\n");
        return metrics.toString();
    }

    /**
     * 获取当前预热状态描述
     * 
     * @return 预热状态描述
     */
    public String getStatusDescription() {
        if (warmupFailures.get() > 0) {
            return "预热失败，失败次数: " + warmupFailures.get();
        }
        if (warmupCompleted.get()) {
            return String.format("预热完成，总耗时: %.2f秒, 用户数: %d, 平均速率: %.2f用户/秒",
                    getWarmupDuration() / 1000.0, addedUsers.get(),
                    avgProcessingRate.get());
        }
        return String.format("预热中，进度: %.2f%%, 已添加: %d/%d 用户, 速率: %d用户/秒, 预计剩余: %d秒",
                getWarmupProgress(), addedUsers.get(), totalUsers.get(),
                avgProcessingRate.get(), estimatedRemainingTime.get() / 1000);
    }

    /**
     * 检查预热是否完成
     * 
     * @return 是否完成预热
     */
    public boolean isWarmupCompleted() {
        return warmupCompleted.get();
    }

    /**
     * 获取预热进度百分比
     * 
     * @return 预热进度百分比 (0-100)
     */
    public double getWarmupProgress() {
        int total = totalUsers.get();
        if (total <= 0) {
            return 0;
        }
        return Math.min(100.0, addedUsers.get() * 100.0 / total);
    }

    /**
     * 获取预热耗时（毫秒）
     * 
     * @return 预热耗时
     */
    public long getWarmupDuration() {
        if (warmupEndTime.get() > 0) {
            return warmupEndTime.get() - warmupStartTime.get();
        }
        if (warmupStartTime.get() > 0) {
            return System.currentTimeMillis() - warmupStartTime.get();
        }
        return 0;
    }

    /**
     * 获取预热统计信息
     * 
     * @return 预热统计信息
     */
    public WarmupStatistics getStatistics() {
        return new WarmupStatistics(
                warmupCompleted.get(),
                totalUsers.get(),
                addedUsers.get(),
                getWarmupProgress(),
                getWarmupDuration(),
                warmupFailures.get(),
                avgProcessingRate.get(),
                estimatedRemainingTime.get());
    }

    /**
     * 预热统计信息内部类
     */
    public static class WarmupStatistics {
        private final boolean completed;
        private final int totalUsers;
        private final int addedUsers;
        private final double progress;
        private final long duration;
        private final int failures;

        // 平均处理速率
        private final long processingRate;

        // 预计剩余时间
        private final long remainingTime;

        public WarmupStatistics(boolean completed, int totalUsers, int addedUsers,
                double progress, long duration, int failures,
                long processingRate, long remainingTime) {
            this.completed = completed;
            this.totalUsers = totalUsers;
            this.addedUsers = addedUsers;
            this.progress = progress;
            this.duration = duration;
            this.failures = failures;
            this.processingRate = processingRate;
            this.remainingTime = remainingTime;
        }

        public long getProcessingRate() {
            return processingRate;
        }

        public long getRemainingTime() {
            return remainingTime;
        }

        public boolean isCompleted() {
            return completed;
        }

        public int getTotalUsers() {
            return totalUsers;
        }

        public int getAddedUsers() {
            return addedUsers;
        }

        public double getProgress() {
            return progress;
        }

        public long getDuration() {
            return duration;
        }

        public int getFailures() {
            return failures;
        }

        @Override
        public String toString() {
            return "WarmupStatistics{" +
                    "completed=" + completed +
                    ", totalUsers=" + totalUsers +
                    ", addedUsers=" + addedUsers +
                    ", progress=" + String.format("%.2f%%", progress) +
                    ", duration=" + duration + "ms" +
                    ", failures=" + failures +
                    ", processingRate=" + processingRate + " users/s" +
                    ", remainingTime=" + remainingTime + "ms" +
                    '}';
        }
    }
}