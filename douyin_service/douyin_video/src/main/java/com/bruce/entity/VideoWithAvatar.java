package com.bruce.entity;

import lombok.Data;


/**
 * (Video)表实体类
 *
 * @author makejava
 * @since 2024-06-25 13:03:24
 */
@SuppressWarnings("serial")
@Data
public class VideoWithAvatar  extends Video {


 private String avatar;

 private String musicAvatar;
 private String nickname;

}

