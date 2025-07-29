package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Permission)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-29 19:59:58
 */
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {

}

