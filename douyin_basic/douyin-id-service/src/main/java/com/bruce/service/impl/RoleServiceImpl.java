package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.PermissionDao;
import com.bruce.dao.RoleDao;
import com.bruce.dao.RolePermissionDao;
import com.bruce.dao.UserRoleDao;
import com.bruce.dto.RoleDTO;
import com.bruce.entity.Permission;
import com.bruce.entity.Role;
import com.bruce.entity.RolePermission;
import com.bruce.entity.UserRole;
import com.bruce.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Role)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 20:00:24
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
    @Autowired
    private UserRoleDao userRoleMapper;

    @Autowired
    private RolePermissionDao rolePermissionMapper;


    @Autowired
    private PermissionDao permissionMapper;

    @Override
    public List<String> getRoleCodesByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.selectList(
                new QueryWrapper<UserRole>().eq("user_id", userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) return Collections.emptyList();

        return this.listByIds(roleIds).stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());
    }

    @Override
    public void createRole(RoleDTO dto) {
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        save(role);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.selectList(
                new QueryWrapper<UserRole>().eq("user_id", userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) return Collections.emptyList();

        return this.listByIds(roleIds);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.delete(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        for (Long pid : permissionIds) {
            rolePermissionMapper.insert(new RolePermission(roleId, pid));
        }
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        List<Long> ids = rolePermissionMapper.selectList(
                new QueryWrapper<RolePermission>().eq("role_id", roleId)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        return permissionMapper.selectBatchIds(ids);
    }

}

