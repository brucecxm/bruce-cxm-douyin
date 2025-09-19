package com.bruce.handler;

import cn.dev33.satoken.exception.SaTokenException;
import com.bruce.annotation.RequiresPermission;
import com.bruce.annotation.RequiresRole;
import com.bruce.utils.AuthUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限注解处理器
 * 处理@RequiresPermission和@RequiresRole注解，进行权限和角色验证
 */
@Aspect
@Component
public class AuthAnnotationHandler {
    
    /**
     * 处理@RequiresPermission注解
     */
    @Around("@annotation(com.bruce.annotation.RequiresPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
        
        String[] permissionCodes = annotation.value();
        boolean all = annotation.all();
        
        // 验证权限
        if (!checkPermission(permissionCodes, all)) {
            throw new SaTokenException("无权限访问");
        }
        
        // 有权限，继续执行
        return joinPoint.proceed();
    }
    
    /**
     * 处理@RequiresRole注解
     */
    @Around("@annotation(com.bruce.annotation.RequiresRole)")
    public Object aroundRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresRole annotation = method.getAnnotation(RequiresRole.class);
        
        String[] roleCodes = annotation.value();
        boolean all = annotation.all();
        
        // 验证角色
        if (!checkRole(roleCodes, all)) {
            throw new SaTokenException("无权限访问");
        }
        
        // 有权限，继续执行
        return joinPoint.proceed();
    }
    
    /**
     * 检查权限
     */
    private boolean checkPermission(String[] permissionCodes, boolean all) {
        if (permissionCodes == null || permissionCodes.length == 0) {
            return true;
        }
        
        if (all) {
            // 需要拥有所有权限
            for (String code : permissionCodes) {
                if (!AuthUtils.hasPermission(code)) {
                    return false;
                }
            }
            return true;
        } else {
            // 只需要拥有任意一个权限
            return AuthUtils.hasAnyPermission(permissionCodes);
        }
    }
    
    /**
     * 检查角色
     */
    private boolean checkRole(String[] roleCodes, boolean all) {
        if (roleCodes == null || roleCodes.length == 0) {
            return true;
        }
        
        if (all) {
            // 需要拥有所有角色
            for (String code : roleCodes) {
                if (!AuthUtils.hasRole(code)) {
                    return false;
                }
            }
            return true;
        } else {
            // 只需要拥有任意一个角色
            return AuthUtils.hasAnyRole(roleCodes);
        }
    }
}