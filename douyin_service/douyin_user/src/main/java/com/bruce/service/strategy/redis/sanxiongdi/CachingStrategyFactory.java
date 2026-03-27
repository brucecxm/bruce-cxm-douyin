package com.bruce.service.strategy.redis.sanxiongdi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CachingStrategyFactory {

    private final ApplicationContext applicationContext;
    private Map<String, CachingStrategy> strategyMap;

    @Autowired
    public CachingStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取缓存策略
     * 
     * @param type 策略类型：empty, bloom, lock
     * @return 缓存策略实现
     */
    public CachingStrategy getStrategy(String type) {
        // 懒加载策略映射，确保所有策略已初始化
        if (strategyMap == null) {
            strategyMap = applicationContext.getBeansOfType(CachingStrategy.class);
        }
        // 如果没有传策略类型或策略类型不存在，默认使用空值缓存策略
        if (type == null || type.isEmpty() || !strategyMap.containsKey(type)) {
            return strategyMap.get("empty");
        }
        return strategyMap.get(type);
    }
}
