package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2025-07-29 20:00:00
 */
@SuppressWarnings("serial")
@Data
public class Permission extends Model<Permission> {
    
    private Long id;
    //权限名称，如视频删除权限
    private String permissionName;
    //权限编码，如video:delete
    private String permissionCode;
    
    private String description;


    }

