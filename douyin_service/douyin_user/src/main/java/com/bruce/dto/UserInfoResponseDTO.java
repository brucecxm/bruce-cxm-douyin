package com.bruce.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponseDTO {
    private Long id;
    private String nickname;
    private String username;
    private String avatar;
    private String userPic;
    private String backImg;
    private String city;
    private String myIntro;
    private String jieshao; // 兼容前端字段
    private String age;
    private String school;
    private String gender;
    private String bio;
    private String birthday;
    private Integer likes;
    private Integer follow;
    private Integer fan;
    
    // 粉丝、关注、互关数量
    private Integer fansCount;
    private Integer followCount;
    private Integer mutualFollowCount;
    
    // 前端兼容性字段
    private Integer followers;
    private Integer following;
    private Integer mutualFollow;
    
    // 视频相关
    private List<VideoDTO> videoList;
    private IPage<VideoDTO> videoPage;
    private Integer total;
}
