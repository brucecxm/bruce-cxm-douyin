package com.bruce.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message")
public class ChatMessage {

    @TableId
    private Long id;                 // 消息ID
    private Long senderId;           // 发送者ID
    private Long receiverId;         // 接收者ID
    private Long groupId;            // 群ID
    private Integer messageType;     // 消息类型（1=文本，2=图片，3=视频，4=语音等）
    private String messageContent;   // 消息内容
    private LocalDateTime sentTime;  // 发送时间
    private Integer status;          // 消息状态（0=未读，1=已读）
    private Boolean isGroup;         // 是否是群聊消息
}
