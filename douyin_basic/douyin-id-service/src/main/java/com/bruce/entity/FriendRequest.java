package com.bruce.entity;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 好友请求表(FriendRequest)表实体类
 *
 * @author makejava
 * @since 2025-04-18 13:52:23
 */
@SuppressWarnings("serial")
@Data
public class FriendRequest extends Model<FriendRequest> {
    //请求ID
    private Long id;
    //请求发起人ID
    private Long fromUserId;
    //请求接收人ID
    private Long toUserId;
    //验证消息
    private String verifyMsg;
    //请求状态：0=待处理，1=已同意，2=已拒绝
    private Integer status;
    //请求时间
    private LocalDateTime createTime;


    }

