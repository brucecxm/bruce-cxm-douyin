package com.bruce.video.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (Video)表实体类
 *
 * @author makejava
 * @since 2024-06-25 13:03:24
 */
@SuppressWarnings("serial")
@Data
public class Video implements Serializable {

    private Integer videoId;

    private Integer authId;

    private Integer musicId;

    private String videoUrl;

    private String videoComment;

    private String videoImg;

    // 新增的属性
    private String videoTitle; // 视频标题

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

    // 新增的 getter 和 setter 方法
    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
