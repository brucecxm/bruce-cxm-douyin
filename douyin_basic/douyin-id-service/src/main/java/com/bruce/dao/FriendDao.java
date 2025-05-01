package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Friend)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-10 20:23:21
 */
@Mapper
public interface FriendDao extends BaseMapper<Friend> {

}

