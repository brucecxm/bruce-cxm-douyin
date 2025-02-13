package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Users)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-13 15:30:48
 */
@Mapper
public interface UsersDao extends BaseMapper<Users> {

}

