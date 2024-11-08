package com.one.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.one.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-05 23:56:51
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

}

