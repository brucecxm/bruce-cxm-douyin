package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.ChatGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatGroupMapper extends BaseMapper<ChatGroup> {
    // 可以根据需要添加自定义的查询方法
}
