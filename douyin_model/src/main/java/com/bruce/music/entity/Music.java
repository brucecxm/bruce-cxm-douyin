package com.bruce.music.entity;


/**
 * (Music)表实体类
 *
 * @author makejava
 * @since 2024-06-25 18:10:39
 */
@SuppressWarnings("serial")
public class Music  {
    
    private String musicAvatar;
    
    private String musicName;
    
    private Integer musicVideoId;


    public String getMusicAvatar() {
        return musicAvatar;
    }

    public void setMusicAvatar(String musicAvatar) {
        this.musicAvatar = musicAvatar;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public Integer getMusicVideoId() {
        return musicVideoId;
    }

    public void setMusicVideoId(Integer musicVideoId) {
        this.musicVideoId = musicVideoId;
    }


    }

