package com.bruce.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendVO {
    private int userId;         // 好友的用户ID
    private String nickname;     // 好友昵称
    private String avatar;       // 好友头像URL
    private String remark;       // 当前用户对好友的备注
    private String gender;       // 性别（可选）
    private LocalDateTime addedTime;  // 加为好友的时间（可选）
}
