package com.bruce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义权限验证注解
 * 用于Controller方法上，验证用户是否拥有指定权限
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    
    /**
     * 权限码数组
     */
    String[] value() default {};
    
    /**
     * 是否需要所有权限
     * true: 需要拥有所有指定的权限
     * false: 只需要拥有任意一个权限
     */
    boolean all() default false;
}