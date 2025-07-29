package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户关系表(UserRelation)表实体类
 *
 * @author makejava
 * @since 2025-07-29 16:59:40
 */
@SuppressWarnings("serial")
@Data
public class UserRelation extends Model<UserRelation> {
    
    private Long id;
    //发起者用户ID
    private Long userId;
    //被关注/添加的用户ID
    private Long targetUserId;
    //关系类型：1-单向关注，2-互相关注，3-单向密友，4-双向密友
    private Integer relationType;
    
    private Date createTime;
    
    private Date updateTime;


    }

