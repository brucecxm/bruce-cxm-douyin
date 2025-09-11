package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.VideoInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频信息表(VideoInfo)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-15 19:58:31
 */
@Mapper
public interface VideoInfoDao extends BaseMapper<VideoInfo> {

}

