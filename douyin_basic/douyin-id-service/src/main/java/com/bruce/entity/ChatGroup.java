package com.bruce.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_group")
public class ChatGroup {

    @TableId
    private Long id;                 // 群ID
    private String groupName;        // 群名称
    private LocalDateTime createdTime;  // 创建时间
    private Long createdBy;          // 创建者ID
}
