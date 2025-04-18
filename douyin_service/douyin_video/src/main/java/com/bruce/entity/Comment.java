package com.bruce.entity;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2024-06-26 23:27:03
 */
@Data
@SuppressWarnings("serial")
public class Comment {
    private Long id;
    private String commentType;  // 评论类型：视频、商品等
    private Boolean isFollow;  // 是否是跟评
    private Long commentId;  // 被评论的对象 ID
    private String content;  // 评论内容
    private List<String> imageUrls;  // 图片URL列表
    private Date createdAt;  // 创建时间
    private Date updatedAt;  // 更新时间

    private String comment;
    
    private Integer commentVideoId;
    
    private Integer commentUserId;


    }

