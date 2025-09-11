package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * (RolePermission)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-29 20:00:38
 */
@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermission> {

}

