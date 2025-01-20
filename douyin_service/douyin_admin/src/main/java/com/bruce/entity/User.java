package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-11-26 17:41:40
 */
@SuppressWarnings("serial")
@Data
public class User extends Model<User> {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String userPic;

    private Date createTime;

    private Date updateTime;

    private String backImg;

    private String city;

    private String jieshao;

    private String age;

    private String school;

    private String permissionId;

    private String roleId;
    }

