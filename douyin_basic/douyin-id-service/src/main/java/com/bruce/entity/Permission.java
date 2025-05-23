package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2025-05-23 17:34:08
 */
@SuppressWarnings("serial")
public class Permission extends Model<Permission> {
    
    private Integer id;
    
    private String permission;
    
    private Integer roleId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}

