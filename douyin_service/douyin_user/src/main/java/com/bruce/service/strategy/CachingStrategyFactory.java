package com.bruce.service.strategy;

import com.bruce.service.strategy.impl.BloomFilterStrategy;
import com.bruce.service.strategy.impl.EmptyCacheStrategy;
import com.bruce.service.strategy.impl.LockStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CachingStrategyFactory {
    @Autowired
    private EmptyCacheStrategy emptyCacheStrategy;
    
    @Autowired
    private BloomFilterStrategy bloomFilterStrategy;
    
    @Autowired
    private LockStrategy lockStrategy;
    
    /**
     * 根据类型获取缓存策略
     * @param type 策略类型：empty, bloom, lock
     * @return 缓存策略实现
     */
    public CachingStrategy getStrategy(String type) {
        if (type == null || type.isEmpty()) {
            // 默认使用空值缓存策略
            return emptyCacheStrategy;
        }
        
        switch (type) {
            case "bloom":
                return bloomFilterStrategy;
            case "lock":
                return lockStrategy;
            case "empty":
            default:
                return emptyCacheStrategy;
        }
    }
}
