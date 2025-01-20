package com.bruce.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface musiconeDao {

    @Select("SELECT c.*,d.*,11 as linkurl FROM music c " +
            "LEFT JOIN video d ON c.music_video_id = d.video_id " +
            "WHERE c.music_id = #{music_id}")
    List<Map> one(@Param("music_id") String music_id);


}
