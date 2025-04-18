package com.bruce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private Long id;                 // 消息ID
    private Long senderId;           // 发送者ID
    private Long receiverId;         // 接收者ID
    private Long groupId;            // 群聊ID，如果是群聊消息则非空
    private Integer messageType;     // 消息类型（1=文本，2=图片，3=视频，4=语音等）
    private String messageContent;   // 消息内容
    private LocalDateTime sentTime;  // 发送时间
    private Boolean isGroup;         // 是否是群聊消息

    // 额外字段：为了方便展示
    private String senderName;       // 发送者的名称（可以从数据库或缓存中获取）
    private String receiverName;     // 接收者的名称（可以从数据库或缓存中获取）
    private String groupName;        // 群聊名称（如果是群聊消息，显示群名）
}
