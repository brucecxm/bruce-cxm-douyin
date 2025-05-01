package com.bruce.dto;

import lombok.Data;

@Data
public class VideoDTO {

    private String videoUrl;       // 视频的URL
    private String videoArticle;   // 视频的描述或标题
    private String username;       // 用户名

    private String userAvatar;     // 用户头像URL
    private String likeNum;        // 点赞数
    private String commentNum;     // 评论数
    private String shareNum;       // 分享数
    private String collectNum;     // 收藏数
    private String musicAvatar;    // 音乐封面URL

    private String musicName;      // 音乐名称


    }


