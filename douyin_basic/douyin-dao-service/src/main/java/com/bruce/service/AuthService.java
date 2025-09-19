package com.bruce.service;

import com.bruce.entity.User;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务类，封装用户认证相关功能
 * 支持网关和用户服务的权限验证逻辑
 */
@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    // Redis中用户信息缓存的过期时间(秒)
    private static final long USER_INFO_EXPIRE_TIME = 600;

    // Redis中权限信息缓存的过期时间(秒)
    private static final long PERMISSION_EXPIRE_TIME = 3600;

    // Redis中角色信息缓存的过期时间(秒)
    private static final long ROLE_EXPIRE_TIME = 3600;

    /**
     * 获取用户权限码列表
     * 优先从Redis缓存获取，缓存不存在则从数据库查询并缓存
     * @param userId 用户ID
     * @return 权限码列表
     */
    public List<String> getUserPermissionCodes(Long userId) {
        // 构建缓存key
        String cacheKey = "auth:permission:codes:" + userId;

        // 尝试从Redis获取权限列表
        String permissionCodesJson = redisUtil.get(cacheKey);
        if (permissionCodesJson != null) {
            try {
                // 反序列化权限列表
                // 注意：这里需要根据实际项目中的JSON工具进行调整
                // 例如使用Jackson、Fastjson等
                // List<String> permissions = JSON.parseArray(permissionCodesJson, String.class);
                // return permissions;
            } catch (Exception e) {
                // 反序列化失败，继续从数据库查询
            }
        }

        // 缓存不存在或失效，从数据库查询
        List<String> permissions = permissionService.getPermissionCodesByUserId(userId);

        // 将查询结果存入Redis缓存
        if (permissions != null && !permissions.isEmpty()) {
            try {
                // 序列化权限列表
                // String json = JSON.toJSONString(permissions);
                // redisUtil.set(cacheKey, json, PERMISSION_EXPIRE_TIME, TimeUnit.SECONDS);
            } catch (Exception e) {
                // 序列化失败，不影响主要功能
            }
        }

        return permissions;
    }

    /**
     * 获取用户角色码列表
     * 优先从Redis缓存获取，缓存不存在则从数据库查询并缓存
     * @param userId 用户ID
     * @return 角色码列表
     */
    public List<String> getUserRoleCodes(Long userId) {
        // 构建缓存key
        String cacheKey = "auth:role:codes:" + userId;

        // 尝试从Redis获取角色列表
        String roleCodesJson = redisUtil.get(cacheKey);
        if (roleCodesJson != null) {
            try {
                // 反序列化角色列表
                // List<String> roles = JSON.parseArray(roleCodesJson, String.class);
                // return roles;
            } catch (Exception e) {
                // 反序列化失败，继续从数据库查询
            }
        }

        // 缓存不存在或失效，从数据库查询
        List<String> roles = roleService.getRoleCodesByUserId(userId);

        // 将查询结果存入Redis缓存
        if (roles != null && !roles.isEmpty()) {
            try {
                // 序列化角色列表
                // String json = JSON.toJSONString(roles);
                // redisUtil.set(cacheKey, json, ROLE_EXPIRE_TIME, TimeUnit.SECONDS);
            } catch (Exception e) {
                // 序列化失败，不影响主要功能
            }
        }

        return roles;
    }

    /**
     * 验证用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限码
     * @return 是否拥有权限
     */
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = getUserPermissionCodes(userId);
        return permissions != null && permissions.contains(permissionCode);
    }

    /**
     * 验证用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleCode 角色码
     * @return 是否拥有角色
     */
    public boolean hasRole(Long userId, String roleCode) {
        List<String> roles = getUserRoleCodes(userId);
        return roles != null && roles.contains(roleCode);
    }

    /**
     * 清除用户的权限缓存
     * 当用户权限发生变更时调用
     * @param userId 用户ID
     */
    public void clearUserPermissionCache(Long userId) {
        redisUtil.del("auth:permission:codes:" + userId);
        redisUtil.del("auth:role:codes:" + userId);
    }

    /**
     * 生成JWT Token（兼容需要JWT的场景）
     * @param user 用户信息
     * @return JWT Token
     */
    public String generateJwtToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }

    /**
     * 验证JWT Token
     * @param token JWT Token
     * @param username 用户名
     * @return 是否有效
     */
    public boolean validateJwtToken(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }
}
