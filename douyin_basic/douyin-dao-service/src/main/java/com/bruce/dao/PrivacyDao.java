package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Privacy;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Privacy)表数据库访问层
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
@Mapper
public interface PrivacyDao extends BaseMapper<Privacy> {

}