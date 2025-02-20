package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Salt;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Salt)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-20 15:01:08
 */
@Mapper
public interface SaltDao extends BaseMapper<Salt> {

}

