package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Permission)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-23 17:34:08
 */
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {

}

