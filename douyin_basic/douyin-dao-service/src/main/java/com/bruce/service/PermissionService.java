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
    public List<String> getPermissionCodesByUserId(Long userId) ;
    public List<Permission> getPermissionsByUserId(Long userId);

}

