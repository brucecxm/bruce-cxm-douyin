package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.ChatLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ChatLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-10 19:54:37
 */
@Mapper
public interface ChatLogDao extends BaseMapper<ChatLog> {

}

