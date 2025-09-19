package com.bruce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义角色验证注解
 * 用于Controller方法上，验证用户是否拥有指定角色
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    
    /**
     * 角色码数组
     */
    String[] value() default {};
    
    /**
     * 是否需要所有角色
     * true: 需要拥有所有指定的角色
     * false: 只需要拥有任意一个角色
     */
    boolean all() default false;
}