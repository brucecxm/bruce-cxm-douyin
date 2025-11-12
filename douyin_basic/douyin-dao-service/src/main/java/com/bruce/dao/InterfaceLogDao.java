package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.InterfaceLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口访问日志表（记录UserController所有接口调用信息）(InterfaceLog)表数据库访问层
 *
 * @author makejava
 * @since 2025-11-12 09:24:28
 */
@Mapper
public interface InterfaceLogDao extends BaseMapper<InterfaceLog> {

}

