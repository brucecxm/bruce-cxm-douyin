package com.bruce.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginStrategyFactory {

    private final ApplicationContext applicationContext;
    private Map<String, LoginStrategy> strategyMap;

    @Autowired
    public LoginStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据登录类型获取登录策略
     * @param loginType 登录类型
     * @return 登录策略实现
     */
    public LoginStrategy getStrategy(String loginType) {
        // 懒加载策略映射，确保所有策略已初始化
        if (strategyMap == null) {
            strategyMap = applicationContext.getBeansOfType(LoginStrategy.class);
        }
        // 如果没有传登录类型或登录类型不存在，默认使用密码登录策略
        if (loginType == null || loginType.isEmpty() || !strategyMap.containsKey(loginType)) {
            return strategyMap.get("password");
        }
        return strategyMap.get(loginType);
    }
}