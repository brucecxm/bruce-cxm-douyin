package com.bruce.entitys;

import com.bruce.entity.Comment;
import lombok.Data;

/**
 * 评论VO（带用户信息）
 */
@Data
public class CommentVO extends Comment {

    // 用户名
    private String username;

    // 用户头像
    private String avatar;
}
