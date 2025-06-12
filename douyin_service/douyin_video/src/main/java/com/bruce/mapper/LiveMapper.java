package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entitys.Live;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveMapper extends BaseMapper<Live> {

    /**
     * 根据状态查询直播列表
     */
    @Select("SELECT id, host_id, host_username, title, cover_url, channel_name, " +
            "status, start_time, end_time, viewer_count, description, " +
            "replay_url, create_time, update_time " +
            "FROM t_live " +
            "WHERE status = #{status} " +
            "ORDER BY create_time DESC")
    List<Live> selectListByStatus(@Param("status") Integer status);

    /**
     * 根据主播ID查询直播列表
     */
    @Select("SELECT id, host_id, host_username, title, cover_url, channel_name, " +
            "status, start_time, end_time, viewer_count, description, " +
            "replay_url, create_time, update_time " +
            "FROM t_live " +
            "WHERE host_id = #{hostId} " +
            "ORDER BY create_time DESC")
    List<Live> selectListByHostId(@Param("hostId") Long hostId);

    /**
     * 查询热门直播列表
     */
    @Select("SELECT id, host_id, host_username, title, cover_url, channel_name, " +
            "status, start_time, end_time, viewer_count, description, " +
            "replay_url, create_time, update_time " +
            "FROM t_live " +
            "WHERE status = 1 " +
            "ORDER BY viewer_count DESC, create_time DESC " +
            "LIMIT #{limit}")
    List<Live> selectHotLiveList(@Param("limit") Integer limit);

    /**
     * 更新直播观看人数
     */
    @Update("UPDATE t_live SET viewer_count = #{viewerCount} WHERE id = #{id}")
    int updateViewerCount(@Param("id") Long id, @Param("viewerCount") Integer viewerCount);

    /**
     * 增加直播观看人数
     */
    @Update("UPDATE t_live SET viewer_count = viewer_count + 1 WHERE id = #{id}")
    int incrementViewerCount(@Param("id") Long id);

    /**
     * 减少直播观看人数
     */
    @Update("UPDATE t_live SET viewer_count = GREATEST(viewer_count - 1, 0) WHERE id = #{id}")
    int decrementViewerCount(@Param("id") Long id);
}
