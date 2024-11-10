package com.bruce.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (ChatLog)表实体类
 *
 * @author makejava
 * @since 2024-11-10 19:54:38
 */
@SuppressWarnings("serial")
public class ChatLog extends Model<ChatLog> {

    private String userId;  // 改为 String 类型

    private String friendId; // 改为 String 类型

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 其他字段和方法...

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }



}
