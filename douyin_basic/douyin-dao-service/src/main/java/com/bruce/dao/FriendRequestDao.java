package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友请求表(FriendRequest)表数据库访问层
 *
 * @author makejava
 * @since 2025-04-18 13:52:23
 */
@Mapper
public interface FriendRequestDao extends BaseMapper<FriendRequest> {

}

