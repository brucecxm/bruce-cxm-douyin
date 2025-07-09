package com.bruce.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-11-19 11:42:30
 */
@SuppressWarnings("serial")
@Data
public class User  {
    private long id;
    private String email;
    private String userPic;
    private String backImg;
    private String city;
    private String myIntro;
    private String age;
    private String school;
    private String username;
    private String gender;
    private String password;
    private String nickname;
    private String avatar;       // 发起人头像URL
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private String permissionId;
    private String roleId;
    private String paymentPassword;
    }

