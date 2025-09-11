package com.bruce.utils;

import org.springframework.stereotype.Component;

@Component
public class SimpleIdGenerator {
    private static long currentId = 0;

    // 基于时间戳和自增序列生成ID
    public synchronized static long generateId() {
        return System.currentTimeMillis() * 1000 + (++currentId);
    }

}
