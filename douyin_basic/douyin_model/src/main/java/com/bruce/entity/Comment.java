package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2025-06-04 15:45:13
 */
@SuppressWarnings("serial")
public class Comment extends Model<Comment> {
    
    private Long commentId;
    
    private String comment;
    
    private Long commentType;
    
    private Long userId;
    
    private Integer fatherId;
    
    private Integer contentId;


    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCommentType() {
        return commentType;
    }

    public void setCommentType(Long commentType) {
        this.commentType = commentType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.commentId;
    }
    }

