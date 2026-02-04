package com.bruce.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendDetailVO {
    // 好友关系信息
    private Long friendId;
    private String remark;
    private LocalDateTime addedTime;
    private Integer status;
    
    // 用户详细信息
    private Long userId;
    private String nickname;
    private String avatar;
    private String gender;
    private String phone;
    private String email;
    private String city;
    private String myIntro;
    private Integer age;
    private String school;
    private String userPic;
    private String backImg;
    private Integer isAnchor;
    private Integer isPrivate;
}