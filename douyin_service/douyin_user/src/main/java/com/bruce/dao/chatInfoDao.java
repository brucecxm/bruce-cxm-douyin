package com.bruce.dao;

import com.bruce.pojo.ChatInfo;
import com.bruce.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface chatInfoDao {
   List<ChatInfo> queryUserInfo(@Param("user_id") String user_id );

   User queryUserInfoone(@Param("user_id") String user_id );




}
