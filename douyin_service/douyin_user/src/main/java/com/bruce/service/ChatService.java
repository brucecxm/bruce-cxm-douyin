package com.bruce.service;

import com.bruce.dto.ChatMessageVO;

import java.util.List;

public interface ChatService {

    // 发送消息（私聊/群聊）
    void sendMessage(Long senderId, Long receiverId, Long groupId, String messageContent, int messageType, boolean isGroup);

    // 获取聊天记录（私聊或群聊）
    List<ChatMessageVO> getChatHistory(Long userId, Long friendId, Long groupId, int pageNo, int pageSize);

    // 创建群聊
    Long createGroup(Long createdBy, String groupName);

    // 添加群成员
    void addGroupMember(Long groupId, Long userId);
}
