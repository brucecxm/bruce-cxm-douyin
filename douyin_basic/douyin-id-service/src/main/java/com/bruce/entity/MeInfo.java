package com.bruce.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-11-19 11:42:30
 */
@SuppressWarnings("serial")
@Data
public class MeInfo {
    List<Video> videoList;
    private int id;

    private String username;
    private String gender;

    private String password;

    private String nickname;

    private String email;

    private String userPic;

    private String avatar;       // 发起人头像URL

    private Date createTime;


    private Date updateTime;


    private String backImg;

    private String city;

    private String jieshao;

    private String age;

    private String school;

}

