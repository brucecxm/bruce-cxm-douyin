package com.bruce.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "视频实体类")
public class Video implements Serializable {
    @ApiModelProperty(value = "视频ID", example = "1")
    private Long videoId;

    private Integer authId;

    private Integer musicId;

    private String videoUrl;

    private String videoComment;

    private String videoImg;

    // 新增的属性
    private String videoTitle; // 视频标题

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
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
