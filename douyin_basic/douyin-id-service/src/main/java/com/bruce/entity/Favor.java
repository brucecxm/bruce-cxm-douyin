package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Favor)表实体类
 *
 * @author makejava
 * @since 2025-06-10 10:37:16
 */
@SuppressWarnings("serial")
public class Favor extends Model<Favor> {
    
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

