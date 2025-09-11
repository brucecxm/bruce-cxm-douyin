package com.bruce.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRequestVO {
    private Long requestId;      // 请求ID
    private int fromUserId;     // 谁发起的请求
    private String nickname;     // 发起人昵称
    private String avatar;       // 发起人头像URL
    private String verifyMsg;    // 验证消息
    private String status;       // 当前状态：待处理/已同意/已拒绝
    private LocalDateTime requestTime;  // 发起时间
}
