package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Like;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Like)表数据库访问层
 *
 * @author makejava
 * @since 2025-06-04 15:26:48
 */
@Mapper
public interface LikeDao extends BaseMapper<Like> {

}

