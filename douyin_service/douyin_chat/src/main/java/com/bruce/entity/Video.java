package com.bruce.entity;

import java.io.Serializable;

/**
 * (Video)实体类
 *
 * @author makejava
 * @since 2024-09-22 15:39:10
 */
public class Video implements Serializable {
    private static final long serialVersionUID = 315479714123161457L;
    
    private Integer videoId;
    
    private Integer authId;
    
    private Integer musicId;
    
    private String videoUrl;
    
    private String videoComment;


    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getMusicId() {
        return musicId;
    }

    public void setMusicId(Integer musicId) {
        this.musicId = musicId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoComment() {
        return videoComment;
    }

    public void setVideoComment(String videoComment) {
        this.videoComment = videoComment;
    }

}

