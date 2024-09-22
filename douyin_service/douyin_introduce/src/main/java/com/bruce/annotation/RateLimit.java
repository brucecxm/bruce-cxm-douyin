package com.bruce.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
//用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int value() default 10; // 默认限制次数
    int period() default 60; // 默认时间窗口（秒）
}
