package com.bruce.entity;


/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2024-06-26 23:27:03
 */
@SuppressWarnings("serial")
public class Comment {
    
    private Integer commentId;
    
    private String comment;
    
    private Integer commentVideoId;
    
    private Integer commentUserId;


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCommentVideoId() {
        return commentVideoId;
    }

    public void setCommentVideoId(Integer commentVideoId) {
        this.commentVideoId = commentVideoId;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }


    }

