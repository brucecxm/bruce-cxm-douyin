package com.bruce.entity;

import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-11-19 11:42:30
 */
@SuppressWarnings("serial")
@Data
public class UserInfo extends User{


    String videoImg;

    // 粉丝数量
    Integer fansCount;

    // 关注数量
    Integer followCount;

    // 互关数量
    Integer mutualFollowCount;

    // 以下属性用于前端兼容性

    /**
     * 粉丝数量（前端使用）
     */
    private Integer followers;

    /**
     * 关注数量（前端使用）
     */
    private Integer following;

    /**
     * 互关数量（前端使用）
     */
    private Integer mutualFollow;

    List<Video> videoList;
    IPage<Video> videoPage;

    private String username;
    private String gender;

    private String password;

    private String nickname;

    private String email;

    private String userPic;

    private String avatar;       // 发起人头像URL



    private String backImg;

    private String city;

    private String myIntro;

    private String age;

    private String school;

}

