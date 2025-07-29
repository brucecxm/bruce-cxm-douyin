package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * (UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-29 20:01:02
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {

}

