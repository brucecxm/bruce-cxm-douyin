package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * 用户黑名单表(UserBlacklist)表实体类
 *
 * @author makejava
 * @since 2025-07-29 17:01:22
 */
@SuppressWarnings("serial")
public class UserBlacklist extends Model<UserBlacklist> {
    
    private Long id;
    //拉黑者用户ID
    private Long userId;
    //被拉黑者用户ID
    private Long targetUserId;
    
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

