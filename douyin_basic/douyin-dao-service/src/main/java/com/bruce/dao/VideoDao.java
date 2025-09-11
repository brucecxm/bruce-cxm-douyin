package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Video;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Video)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-25 13:03:24
 */
@Mapper
public interface VideoDao extends BaseMapper<Video> {


}

