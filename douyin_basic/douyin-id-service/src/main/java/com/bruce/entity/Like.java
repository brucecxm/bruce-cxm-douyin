package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (Like)表实体类
 *
 * @author makejava
 * @since 2025-06-04 15:26:49
 */
@SuppressWarnings("serial")
public class Like extends Model<Like> {
    
    private Integer userId;
    
    private Integer commentId;
    
    private Integer commentType;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

}

