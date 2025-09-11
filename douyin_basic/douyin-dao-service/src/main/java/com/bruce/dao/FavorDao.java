package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Favor;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Favor)表数据库访问层
 *
 * @author makejava
 * @since 2025-06-10 10:37:16
 */
@Mapper
public interface FavorDao extends BaseMapper<Favor> {

}

