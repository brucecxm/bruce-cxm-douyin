package com.bruce.utils;


import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {
    // 系统开始时间
    private final long twepoch = 1288834974657L;

    // 工作机器ID和数据中心ID所占的位数
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;

    // 最大值（每个字段的最大值）
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // 移动位数
    private final long timestampLeftShift = workerIdBits + datacenterIdBits;
    private final long workerIdShift = datacenterIdBits;
    private final long datacenterIdShift = 0L;

    // 序列号，支持每毫秒产生4096个ID
    private final long sequenceBits = 12L;
    private final long maxSequence = -1L ^ (-1L << sequenceBits);

    // 当前时间戳
    private long lastTimestamp = -1L;

    private long workerId; // 工作机器ID
    private long datacenterId; // 数据中心ID
    private long sequence = 0L; // 序列号

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenterId can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // 生成ID
    public synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上次生成ID的时间，说明系统时钟回拨，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        // 如果在同一毫秒内，生成的ID序列号超过最大值，等待下一毫秒
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 通过位运算组合ID的各部分
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    // 等待直到下一毫秒
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
