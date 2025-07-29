package com.bruce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.UserRole;

import java.util.List;

/**
 * (UserRole)表服务接口
 *
 * @author makejava
 * @since 2025-07-29 20:01:02
 */
public interface UserRoleService extends IService<UserRole> {
    void assignRoles(Long userId, List<Long> roleIds) ;

}

