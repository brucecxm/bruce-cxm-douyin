package com.bruce.service.impl;

import com.bruce.entity.User;
import com.bruce.service.AuthService;
import com.bruce.service.PermissionService;
import com.bruce.service.RoleService;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * 实现用户认证相关功能
 */
@Service
public class AuthServiceImpl extends AuthService {



    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // Redis中用户信息缓存的过期时间(秒)
    private static final long USER_INFO_EXPIRE_TIME = 600;

    // Redis中权限信息缓存的过期时间(秒)
    private static final long PERMISSION_EXPIRE_TIME = 3600;

    // Redis中角色信息缓存的过期时间(秒)
    private static final long ROLE_EXPIRE_TIME = 3600;

    @Override
    public List<String> getUserPermissionCodes(Long userId) {
        // 构建缓存key
        String cacheKey = "auth:permission:codes:" + userId;

        // 尝试从Redis获取权限列表
        String permissionCodesJson = redisUtil.get(cacheKey);
        if (permissionCodesJson != null) {
            try {
                // 使用Jackson反序列化权限列表
                List<String> permissions = objectMapper.readValue(permissionCodesJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                return permissions;
            } catch (JsonProcessingException e) {
                // 反序列化失败，继续从数据库查询
            }
        }

        // 缓存不存在或失效，从数据库查询
        List<String> permissions = permissionService.getPermissionCodesByUserId(userId);
        if (permissions == null) {
            permissions = Collections.emptyList();
        }

        // 将查询结果存入Redis缓存
        try {
            // 序列化权限列表
            String json = objectMapper.writeValueAsString(permissions);
            redisUtil.set(cacheKey, json, PERMISSION_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            // 序列化失败，不影响主要功能
        }

        return permissions;
    }

    @Override
    public List<String> getUserRoleCodes(Long userId) {
        // 构建缓存key
        String cacheKey = "auth:role:codes:" + userId;

        // 尝试从Redis获取角色列表
        String roleCodesJson = redisUtil.get(cacheKey);
        if (roleCodesJson != null) {
            try {
                // 使用Jackson反序列化角色列表
                List<String> roles = objectMapper.readValue(roleCodesJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                return roles;
            } catch (JsonProcessingException e) {
                // 反序列化失败，继续从数据库查询
            }
        }

        // 缓存不存在或失效，从数据库查询
        List<String> roles = roleService.getRoleCodesByUserId(userId);
        if (roles == null) {
            roles = Collections.emptyList();
        }

        // 将查询结果存入Redis缓存
        try {
            // 序列化角色列表
            String json = objectMapper.writeValueAsString(roles);
            redisUtil.set(cacheKey, json, ROLE_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            // 序列化失败，不影响主要功能
        }

        return roles;
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = getUserPermissionCodes(userId);
        return permissions != null && permissions.contains(permissionCode);
    }

    @Override
    public boolean hasRole(Long userId, String roleCode) {
        List<String> roles = getUserRoleCodes(userId);
        return roles != null && roles.contains(roleCode);
    }

    @Override
    public void clearUserPermissionCache(Long userId) {
        redisUtil.del("auth:permission:codes:" + userId);
        redisUtil.del("auth:role:codes:" + userId);
    }

    @Override
    public String generateJwtToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }

    @Override
    public boolean validateJwtToken(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }
}
