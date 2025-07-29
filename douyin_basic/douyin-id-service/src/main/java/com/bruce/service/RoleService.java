package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.dto.RoleDTO;
import com.bruce.entity.Permission;
import com.bruce.entity.Role;

import java.util.List;

/**
 * (Role)表服务接口
 *
 * @author makejava
 * @since 2025-07-29 20:00:24
 */
public interface RoleService extends IService<Role> {
    public List<String> getRoleCodesByUserId(Long userId) ;
    public void createRole(RoleDTO dto) ;
    public List<Role>  getRolesByUserId(Long userId) ;
        public void assignPermissions(Long roleId, List<Long> permissionIds);
            public List<Permission> getPermissionsByRoleId(Long roleId) ;

}

