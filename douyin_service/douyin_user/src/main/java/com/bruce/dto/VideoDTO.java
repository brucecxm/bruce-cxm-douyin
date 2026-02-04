package com.bruce.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private Long id; // 兼容前端字段
    private Long videoId;
    private Long userId;
    private Long musicId;
    private String url;
    private String content;
    private String title;
    private String cover;
    private Integer type;
    private Integer status;
    
    // 前端展示用字段
    private Integer playCount;
    private Integer likeCount;
}
