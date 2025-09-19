package com.bruce.handler;

import cn.dev33.satoken.stp.StpInterface;
import com.bruce.service.PermissionService;
import com.bruce.service.RoleService;
import com.bruce.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;
    // 获取用户角色列表
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 关键修改：先转为 String，再解析为 Long
        Long userId = Long.parseLong(loginId.toString());
        
        // 从数据库查询用户角色
        List<String> roleList = roleService.getRoleCodesByUserId(userId);
        
        // 缓存角色信息到Redis
        if (roleList != null) {
            AuthUtils.cacheUserRoles(userId, roleList);
        }
        
        return roleList;
    }

    // 获取用户权限列表
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 关键修改：先转为 String，再解析为 Long
        Long userId = Long.parseLong(loginId.toString());
        
        // 从数据库查询用户权限
        List<String> permList = permissionService.getPermissionCodesByUserId(userId);
        
        // 缓存权限信息到Redis
        if (permList != null) {
            AuthUtils.cacheUserPermissions(userId, permList);
        }
        
        return permList;
    }
}