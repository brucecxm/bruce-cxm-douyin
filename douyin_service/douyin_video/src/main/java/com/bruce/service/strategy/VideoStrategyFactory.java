package com.bruce.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 视频策略工厂
 */
@Component
public class VideoStrategyFactory {

    private final ApplicationContext applicationContext;
    private Map<String, VideoStrategy> strategyMap;

    /**
     * 构造方法自动注入 ApplicationContext
     */
    @Autowired
    public VideoStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据 videoType 获取对应的策略
     *
     * @param videoType 视频类型
     * @return 对应的视频策略，不存在则返回默认策略
     */
    public VideoStrategy getStrategy(String videoType) {
        // 第一次调用时加载所有策略
        if (strategyMap == null) {
            strategyMap = applicationContext.getBeansOfType(VideoStrategy.class);
        }

        // 拼接 bean 名称
        String strategyBeanName = "videoStrategy" + videoType;

        // 获取策略，没有就返回默认策略（必须保证存在 defaultVideoStrategy）
        VideoStrategy strategy = strategyMap.get(strategyBeanName);
        if (strategy == null) {
            strategy = strategyMap.get("defaultVideoStrategy");
        }

        return strategy;
    }
}
