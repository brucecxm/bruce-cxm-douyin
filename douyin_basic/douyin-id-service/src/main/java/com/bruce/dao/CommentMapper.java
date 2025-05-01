package com.bruce.dao;

import com.bruce.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    // 获取评论列表，基于视频ID
    @Select("SELECT c.comment_id, c.last_id, c.comment,c.comment_user_id as userId,u.username,u.user_pic as avatar,10 as likes, c.createtime " +
            "FROM comment c " +
            "inner join user u on c.comment_user_id = u.id" +
            "   WHERE c.comment_video_id = #{videoId} and last_id = '-1'")
    List<Map> getCommentid(@Param("videoId") int videoId);

    // 获取基于 last_id 的评论，使用 like 匹配
    @Select("SELECT comment_id, last_id, comment, createtime " +
            "FROM comment " +
            "WHERE last_id LIKE CONCAT(#{fuid}, '%') " +
            "ORDER BY createtime DESC")
    List<CommentDto> getCommentide(@Param("fuid") Integer fuid);
}
