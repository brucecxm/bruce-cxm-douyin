package com.bruce.dao;

import com.bruce.pojo.ChatInfo;
import com.bruce.pojo.UserVideoDTO;
import com.bruce.user.pojo.User;
import com.bruce.video.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface chatInfoDao {
   List<ChatInfo> queryUserInfo(@Param("user_id") String user_id );

   User queryUserInfoone(@Param("user_id") String user_id );

   @Select("SELECT a.nickname, a.user_pic, b.video_url, b.video_img " +
           "FROM user a LEFT JOIN video b ON a.id = b.auth_id " +
           "WHERE a.id = #{userId}")
   @Results({
           @Result(property = "nickname", column = "nickname"),
           @Result(property = "user_pic", column = "user_pic"),
           // 这里可以添加处理 video 的逻辑
   })
   List<UserVideoDTO> getUserVideosById(int userId);




}
