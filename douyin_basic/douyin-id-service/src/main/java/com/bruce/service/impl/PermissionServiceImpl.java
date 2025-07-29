package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.PermissionDao;
import com.bruce.dao.RoleDao;
import com.bruce.dao.RolePermissionDao;
import com.bruce.dao.UserRoleDao;
import com.bruce.entity.Permission;
import com.bruce.entity.RolePermission;
import com.bruce.entity.UserRole;
import com.bruce.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Permission)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 20:00:01
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {
    @Autowired
    private UserRoleDao userRoleMapper;
    @Autowired
    private RolePermissionDao rolePermissionMapper;
    @Autowired
    private RoleDao roleMapper;
    @Autowired
    private PermissionDao permissionMapper;

    @Override
    public List<String> getPermissionCodesByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.selectList(
                new QueryWrapper<UserRole>().eq("user_id", userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionMapper.selectList(
                new QueryWrapper<RolePermission>().in("role_id", roleIds)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        if (permissionIds.isEmpty()) return Collections.emptyList();

        return this.listByIds(permissionIds).stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.selectList(
                new QueryWrapper<UserRole>().eq("user_id", userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) return new ArrayList<>();

        List<Long> permissionIds = rolePermissionMapper.selectList(
                new QueryWrapper<RolePermission>().in("role_id", roleIds)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        return permissionMapper.selectBatchIds(permissionIds);
    }

}

