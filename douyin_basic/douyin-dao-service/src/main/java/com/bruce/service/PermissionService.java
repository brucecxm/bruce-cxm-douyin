package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Permission;

import java.util.List;

/**
 * (Permission)表服务接口
 *
 * @author makejava
 * @since 2025-07-29 20:00:01
 */
public interface PermissionService extends IService<Permission> {
    
    /**
     * 根据用户ID获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    List<String> getPermissionCodesByUserId(Long userId);
    
    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByUserId(Long userId);
    
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限码
     * @return 是否拥有权限
     */
    boolean hasPermission(Long userId, String permissionCode);
    
    /**
     * 检查用户是否拥有任意一个指定权限
     * @param userId 用户ID
     * @param permissionCodes 权限码数组
     * @return 是否拥有任意一个权限
     */
    boolean hasAnyPermission(Long userId, String[] permissionCodes);
    
    /**
     * 检查用户是否拥有所有指定权限
     * @param userId 用户ID
     * @param permissionCodes 权限码数组
     * @return 是否拥有所有权限
     */
    boolean hasAllPermissions(Long userId, String[] permissionCodes);
}

