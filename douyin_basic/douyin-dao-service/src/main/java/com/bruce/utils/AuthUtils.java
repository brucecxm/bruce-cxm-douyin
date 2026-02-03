package com.bruce.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.bruce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 认证工具类
 * 提供静态方法进行权限和角色验证
 */
@Component
public class AuthUtils {
    
    private static AuthUtils instance;
    
    @Autowired
    private AuthService authService;
    
    @PostConstruct
    public void init() {
        instance = this;
    }
    
    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        Long loginUserId = null;
        try {
            StpUtil.checkLogin();
            Object userIdObj = StpUtil.getLoginId();
            if (userIdObj instanceof Long) {
                loginUserId = (Long) userIdObj;
            } else if (userIdObj instanceof Integer) {
                loginUserId = ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                loginUserId = Long.parseLong((String) userIdObj);
            }
        } catch (cn.dev33.satoken.exception.NotLoginException e) {
            // 用户未登录，loginUserId保持为null
        }
        return loginUserId;
    }
    
    /**
     * 检查当前用户是否登录
     * @return 是否登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
    
    /**
     * 获取当前用户的权限列表
     * @return 权限列表
     */
    public static List<String> getCurrentUserPermissions() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return instance.authService.getUserPermissionCodes(userId);
        }
        return null;
    }
    
    /**
     * 获取当前用户的角色列表
     * @return 角色列表
     */
    public static List<String> getCurrentUserRoles() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return instance.authService.getUserRoleCodes(userId);
        }
        return null;
    }
    
    /**
     * 检查当前用户是否拥有指定权限
     * @param permissionCode 权限码
     * @return 是否拥有权限
     */
    public static boolean hasPermission(String permissionCode) {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return instance.authService.hasPermission(userId, permissionCode);
        }
        return false;
    }
    
    /**
     * 检查当前用户是否拥有指定角色
     * @param roleCode 角色码
     * @return 是否拥有角色
     */
    public static boolean hasRole(String roleCode) {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return instance.authService.hasRole(userId, roleCode);
        }
        return false;
    }
    
    /**
     * 检查当前用户是否拥有指定权限中的任意一个
     * @param permissionCodes 权限码数组
     * @return 是否拥有任意一个权限
     */
    public static boolean hasAnyPermission(String... permissionCodes) {
        Long userId = getCurrentUserId();
        if (userId != null && permissionCodes != null) {
            for (String code : permissionCodes) {
                if (instance.authService.hasPermission(userId, code)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 检查当前用户是否拥有指定角色中的任意一个
     * @param roleCodes 角色码数组
     * @return 是否拥有任意一个角色
     */
    public static boolean hasAnyRole(String... roleCodes) {
        Long userId = getCurrentUserId();
        if (userId != null && roleCodes != null) {
            for (String code : roleCodes) {
                if (instance.authService.hasRole(userId, code)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 检查指定用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限码
     * @return 是否拥有权限
     */
    public static boolean hasPermission(Long userId, String permissionCode) {
        if (userId != null) {
            return instance.authService.hasPermission(userId, permissionCode);
        }
        return false;
    }
    
    /**
     * 检查指定用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleCode 角色码
     * @return 是否拥有角色
     */
    public static boolean hasRole(Long userId, String roleCode) {
        if (userId != null) {
            return instance.authService.hasRole(userId, roleCode);
        }
        return false;
    }
    
    /**
     * 清除用户的权限缓存
     * 当用户权限发生变更时调用
     * @param userId 用户ID
     */
    public static void clearUserPermissionCache(Long userId) {
        if (userId != null) {
            instance.authService.clearUserPermissionCache(userId);
        }
    }
}