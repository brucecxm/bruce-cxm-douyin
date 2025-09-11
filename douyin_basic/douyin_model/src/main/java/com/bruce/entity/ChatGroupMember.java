package com.bruce.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_group_member")
public class ChatGroupMember {

    @TableId
    private Long id;               // ID
    private Long groupId;          // 群ID
    private Long userId;           // 用户ID
    private LocalDateTime joinTime; // 加入时间
    private Integer status;         // 状态（1=正常，0=退出）
}
