package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.UserRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户关系表(UserRelation)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-29 16:59:40
 */
@Mapper
public interface UserRelationDao extends BaseMapper<UserRelation> {

}

