package com.bruce.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.bruce.dict.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证工具类，提供权限和角色管理的辅助方法
 */
@Component
public class AuthUtils {

    private static StringRedisTemplate staticStringRedisTemplate;
    private static ObjectMapper staticObjectMapper;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        AuthUtils.staticStringRedisTemplate = stringRedisTemplate;
    }
    
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        AuthUtils.staticObjectMapper = objectMapper;
    }

    /**
     * 清除用户的权限缓存
     * @param userId 用户ID
     */
    public static void clearPermissionCache(Long userId) {
        // 清除Redis中的权限缓存
        staticStringRedisTemplate.delete(Constant.User.USER_PERMISSIONS_PREFIX + userId);
        staticStringRedisTemplate.delete(Constant.User.USER_ROLES_PREFIX + userId);
        
        // 清除当前会话中的权限缓存
        if (StpUtil.isLogin()) {
            SaSession session = StpUtil.getSession();
            session.delete(Constant.User.SESSION_PERMISSIONS_KEY);
            session.delete(Constant.User.SESSION_ROLES_KEY);
        }
    }

    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    public static List<String> getRoleList(Long userId) {
        // 先从Redis获取缓存的角色列表
        String rolesKey = Constant.User.USER_ROLES_PREFIX + userId;
        String rolesJson = staticStringRedisTemplate.opsForValue().get(rolesKey);
        
        if (rolesJson != null) {
            try {
                return staticObjectMapper.readValue(rolesJson, 
                    staticObjectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            } catch (Exception e) {
                // 解析失败则从数据库获取
                return new ArrayList<>();
            }
        }
        
        // 如果当前已登录，尝试从会话中获取
        if (StpUtil.isLogin() && String.valueOf(userId).equals(StpUtil.getLoginId())) {
            SaSession session = StpUtil.getSession();
            List<String> roles = (List<String>) session.get(Constant.User.SESSION_ROLES_KEY);
            if (roles != null) {
                // 缓存到Redis
                try {
                    staticStringRedisTemplate.opsForValue().set(rolesKey, staticObjectMapper.writeValueAsString(roles), 1, TimeUnit.HOURS);
                } catch (JsonProcessingException e) {
                    // 忽略序列化异常，仍然返回角色列表
                }
                return roles;
            }
        }
        
        // 都没有则返回空列表，实际数据由StpInterfaceImpl提供
        return new ArrayList<>();
    }

    /**
     * 获取用户的权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    public static List<String> getPermissionList(Long userId) {
        // 先从Redis获取缓存的权限列表
        String permissionsKey = Constant.User.USER_PERMISSIONS_PREFIX + userId;
        String permissionsJson = staticStringRedisTemplate.opsForValue().get(permissionsKey);
        
        if (permissionsJson != null) {
            try {
                return staticObjectMapper.readValue(permissionsJson, 
                    staticObjectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            } catch (Exception e) {
                // 解析失败则从数据库获取
                return new ArrayList<>();
            }
        }
        
        // 如果当前已登录，尝试从会话中获取
        if (StpUtil.isLogin() && String.valueOf(userId).equals(StpUtil.getLoginId())) {
            SaSession session = StpUtil.getSession();
            List<String> permissions = (List<String>) session.get(Constant.User.SESSION_PERMISSIONS_KEY);
            if (permissions != null) {
                // 缓存到Redis
                try {
                    staticStringRedisTemplate.opsForValue().set(permissionsKey, staticObjectMapper.writeValueAsString(permissions), 1, TimeUnit.HOURS);
                } catch (JsonProcessingException e) {
                    // 忽略序列化异常，仍然返回权限列表
                }
                return permissions;
            }
        }
        
        // 都没有则返回空列表，实际数据由StpInterfaceImpl提供
        return new ArrayList<>();
    }

    /**
     * 缓存用户角色到Redis
     * @param userId 用户ID
     * @param roles 角色列表
     */
    public static void cacheUserRoles(Long userId, List<String> roles) {
        String rolesKey = Constant.User.USER_ROLES_PREFIX + userId;
        try {
            staticStringRedisTemplate.opsForValue().set(rolesKey, staticObjectMapper.writeValueAsString(roles), 1, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            // 序列化失败记录日志，但不影响主流程
        }
    }

    /**
     * 缓存用户权限到Redis
     * @param userId 用户ID
     * @param permissions 权限列表
     */
    public static void cacheUserPermissions(Long userId, List<String> permissions) {
        String permissionsKey = Constant.User.USER_PERMISSIONS_PREFIX + userId;
        try {
            staticStringRedisTemplate.opsForValue().set(permissionsKey, staticObjectMapper.writeValueAsString(permissions), 1, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            // 序列化失败记录日志，但不影响主流程
        }
    }

    /**
     * 检查用户是否有权限
     * @param userId 用户ID
     * @param permission 权限码
     * @return 是否有权限
     */
    public static boolean hasPermission(Long userId, String permission) {
        List<String> permissions = getPermissionList(userId);
        return permissions.contains(permission);
    }

    /**
     * 检查用户是否有任意一个指定的权限
     * @param userId 用户ID
     * @param permissions 权限码数组
     * @return 是否有任意权限
     */
    public static boolean hasAnyPermission(Long userId, String... permissions) {
        List<String> userPermissions = getPermissionList(userId);
        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户是否有指定角色
     * @param userId 用户ID
     * @param role 角色标识
     * @return 是否有角色
     */
    public static boolean hasRole(Long userId, String role) {
        List<String> roles = getRoleList(userId);
        return roles.contains(role);
    }

    /**
     * 检查用户是否有任意一个指定的角色
     * @param userId 用户ID
     * @param roles 角色标识数组
     * @return 是否有任意角色
     */
    public static boolean hasAnyRole(Long userId, String... roles) {
        List<String> userRoles = getRoleList(userId);
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }
}