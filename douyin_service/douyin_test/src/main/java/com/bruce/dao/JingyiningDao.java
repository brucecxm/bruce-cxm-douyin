package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Jingyining;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * (Jingyining)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-12 19:36:28
 */
@Mapper
public interface JingyiningDao extends BaseMapper<Jingyining> {

    @Select("select * from user u inner join jingyining j on j.id = u.id")
    void lianbiaochaxun();






}

