package com.one.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.one.entity.Nav;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Nav)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-06 00:40:08
 */
@Mapper
public interface NavDao extends BaseMapper<Nav> {

}

