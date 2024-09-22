package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.music.entity.Music;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Music)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-25 18:10:38
 */
@Mapper
public interface MusicDao extends BaseMapper<Music> {

}

