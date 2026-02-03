package com.bruce.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginStrategyFactory {

    private final Map<String, LoginStrategy> strategyMap;

    @Autowired
    public LoginStrategyFactory(ApplicationContext applicationContext) {
        // 自动注入所有 LoginStrategy 实现类，key 为 @Component 注解的 value
        this.strategyMap = applicationContext.getBeansOfType(LoginStrategy.class);
    }

    /**
     * 根据登录类型获取登录策略
     * @param loginType 登录类型
     * @return 登录策略实现
     */
    public LoginStrategy getStrategy(String loginType) {
        // 如果没有传登录类型或登录类型不存在，默认使用密码登录策略
        if (loginType == null || loginType.isEmpty() || !strategyMap.containsKey(loginType)) {
            return strategyMap.get("password");
        }
        return strategyMap.get(loginType);
    }
}