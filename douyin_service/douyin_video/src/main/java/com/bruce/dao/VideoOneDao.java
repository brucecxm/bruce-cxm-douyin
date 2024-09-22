package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.video.entity.Video;
import com.bruce.video.entity.VideoOne;
import com.bruce.video.entity.test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Video)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-25 13:03:24
 */
@Mapper
public interface VideoOneDao {

    List<VideoOne> queryVideoInfo(@Param("videoId") int videoId);



}

