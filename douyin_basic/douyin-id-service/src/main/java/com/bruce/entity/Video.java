package com.bruce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("video")  // 如果表名是 video
public class Video implements Serializable {
    @ApiModelProperty(value = "视频ID", example = "1")
    @TableId(type = IdType.AUTO) // 必须添加主键注解
    private Long videoId;
    @TableField("auth_id")  // 指定数据库字段名
    private Integer authId;

    private Integer musicId;

    private String videoUrl;

    private String videoComment;

    private String videoImg;

    // 新增的属性
    private String videoTitle; // 视频标题
    private String videoType; // 视频标题


}
