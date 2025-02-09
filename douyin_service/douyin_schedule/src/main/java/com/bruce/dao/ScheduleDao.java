package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Schedule)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-22 00:37:54
 */
@Mapper
public interface ScheduleDao extends BaseMapper<Schedule> {

}

