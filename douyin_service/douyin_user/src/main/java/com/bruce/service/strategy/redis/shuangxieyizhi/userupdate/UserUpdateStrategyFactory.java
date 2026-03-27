package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserUpdateStrategyFactory {

    private final ApplicationContext applicationContext;
    private Map<String, UserUpdateStrategy> strategyMap;

    @Autowired
    public UserUpdateStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public UserUpdateStrategy getStrategy(String strategyType) {
        if (strategyMap == null) {
            strategyMap = applicationContext.getBeansOfType(UserUpdateStrategy.class);
        }

        if (strategyType == null || strategyType.isEmpty() || !strategyMap.containsKey(strategyType)) {
            return strategyMap.get("delayDoubleDelete");
        }

        return strategyMap.get(strategyType);
    }
}
