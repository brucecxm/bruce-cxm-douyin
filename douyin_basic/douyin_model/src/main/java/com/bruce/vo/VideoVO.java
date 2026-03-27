package com.bruce.vo;

import lombok.Data;

@Data
public class VideoVO {
    private Long videoId;
    private String url;
    private String content;
    private String cover;
    private Long userId;

    private String username;
    private String userAvatar;

    private String music;
    private String musicAvatar;
    private int musicId;

    private Long likeNum;
    private Long commentNum;
    private Long shareNum;
    private Long collectNum;
    private Integer type;
    private Integer status;
    private String title;

    private boolean isLiked;
    private boolean isCollected;
}
