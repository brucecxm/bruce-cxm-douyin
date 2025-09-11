package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * 视频信息表(VideoInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-15 19:58:31
 */
@SuppressWarnings("serial")
public class VideoInfo extends Model<VideoInfo> {
    //视频ID
    private Long id;
    //视频标题
    private String videoTitle;
    //文件哈希值
    private String fileHash;
    //m3u8文件路径
    private String m3u8MinioPath;
    //视频时长(秒)
    private Integer duration;
    //文件大小(字节)
    private Long fileSize;
    //上传时间
    private Date uploadTime;
    //状态(0:转码中,1:可用,2:失效)
    private Integer status;
    //上传用户ID
    private Long userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getM3u8MinioPath() {
        return m3u8MinioPath;
    }

    public void setM3u8MinioPath(String m3u8MinioPath) {
        this.m3u8MinioPath = m3u8MinioPath;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

