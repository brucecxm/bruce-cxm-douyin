package com.bruce.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 视频策略工厂
 */
@Component
public class VideoStrategyFactory {

    private final Map<String, VideoStrategy> strategyMap;

    /**
     * 构造方法自动注入所有VideoStrategy实现
     * Spring会自动将所有VideoStrategy类型的bean注入到Map中
     * key为bean名称，value为对应的策略实现
     */
    @Autowired
    public VideoStrategyFactory(Map<String, VideoStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 根据videoType获取对应的策略
     * 
     * @param videoType 视频类型
     * @return 对应的视频策略
     */
    public VideoStrategy getStrategy(String videoType) {
        // 根据videoType构建对应的策略bean名称
        String strategyBeanName = "videoStrategy" + videoType;
        // 从map中获取策略，如果不存在则使用默认策略
        return strategyMap.getOrDefault(strategyBeanName, strategyMap.get("defaultVideoStrategy"));
    }
}
