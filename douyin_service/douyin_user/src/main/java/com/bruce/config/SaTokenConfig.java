package com.bruce.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 * 提供Sa-Token的核心配置，支持JWT模式
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    /**
     * 注册Sa-Token拦截器，配置拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加Sa-Token拦截器，拦截所有路径
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico", "/user/getVerificationCode", "/user/register", "/user/doLogin"); // 排除不需要拦截的路径
    }
    
    /**
     * 配置JWT模式的StpLogic
     * 当需要使用JWT进行认证时启用
     */
    @Bean
    @ConditionalOnProperty(name = "sa-token.is-jwt", havingValue = "true", matchIfMissing = false)
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}