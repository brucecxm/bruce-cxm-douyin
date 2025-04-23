package com.bruce.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChatMessageRequest {

    @NotNull(message = "发送者ID不能为空")
    private Long senderId;          // 发送者ID

    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;        // 接收者ID，如果是群聊消息，接收者为null，改为groupId

    private Long groupId;           // 群聊ID，如果是群聊消息，传递群ID

    @NotNull(message = "消息类型不能为空")
    private Integer messageType;    // 消息类型（1=文本，2=图片，3=视频，4=语音等）

    @NotNull(message = "消息内容不能为空")
    @Size(min = 1, max = 1024, message = "消息内容长度应在1到1024个字符之间")
    private String messageContent;  // 消息内容

    private Boolean isGroup = false;        // 是否是群聊消息，默认是单聊，群聊设置为true

    // Getter 和 Setter 方法
    public boolean isGroup() {
        return isGroup != null && isGroup;  // 确保 isGroup 不是 null，避免 NullPointerException
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
