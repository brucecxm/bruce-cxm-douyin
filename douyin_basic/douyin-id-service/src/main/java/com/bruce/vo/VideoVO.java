package com.bruce.vo;

import lombok.Data;

@Data
public class VideoVO {
    private Long videoId;
    private String videoUrl;
    private String videoComment;
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
}
