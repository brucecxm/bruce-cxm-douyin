package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.UserBlacklist;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户黑名单表(UserBlacklist)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-29 17:01:21
 */
@Mapper
public interface UserBlacklistDao extends BaseMapper<UserBlacklist> {

}

