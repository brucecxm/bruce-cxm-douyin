package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Friend)表实体类
 *
 * @author makejava
 * @since 2024-11-10 20:23:22
 */
@SuppressWarnings("serial")
public class Friend extends Model<Friend> {
    
    private Integer userId;
    
    private Integer friendId;
    
    private Integer status;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

