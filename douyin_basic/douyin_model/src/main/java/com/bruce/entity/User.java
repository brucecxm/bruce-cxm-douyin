package com.bruce.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-11-19 11:42:30
 */
@SuppressWarnings("serial")
@Data
public class User {
    private Long id;
    private String email;
    private String userPic;
    private String backImg;
    private String city;
    private String myIntro;
    private String age;
    private String school;
    private String username;
    private String gender;
    private String phone;
    private String password;
    private String nickname;
    private String avatar; // 发起人头像URL
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String permissionId;
    private String roleId;
    private String paymentPassword;
    private Boolean isAnchor;
    private int isPrivate;
    // 最后登录时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    // 钱包余额
    private BigDecimal balance;

    // *****下面是新家的 适配商城用的
    // *****下面是新家的 适配商城用的
    // *****下面是新家的 适配商城用的
    // *****下面是新家的 适配商城用的

    private Date birthday;
    private String bio;

    private Date createdAt;

    private Date updatedAt;

    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    protected void onUpdate() {
        updatedAt = new Date();
    }

}
