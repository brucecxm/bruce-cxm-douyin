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


}
