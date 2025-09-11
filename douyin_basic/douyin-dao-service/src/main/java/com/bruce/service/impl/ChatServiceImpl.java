package com.bruce.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.dao.ChatGroupMapper;
import com.bruce.dao.ChatGroupMemberMapper;
import com.bruce.dao.ChatMessageMapper;
import com.bruce.pojo.ChatMessageVO;
import com.bruce.entity.ChatGroup;
import com.bruce.entity.ChatGroupMember;
import com.bruce.entity.ChatMessage;
import com.bruce.service.ChatService;
import com.bruce.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatGroupMapper chatGroupMapper;

    @Autowired
    private ChatGroupMemberMapper chatGroupMemberMapper;
    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void sendMessageWebsocket(Long senderId, Long receiverId, Long groupId, String messageContent, String messageType, boolean isGroup) throws Exception {
        // 保存消息到数据库等逻辑
        String message = senderId + ": " + messageContent;

        if (isGroup) {
            // 群聊消息，广播给所有群成员
            chatWebSocketHandler.broadcastMessage(message);
        } else {
            // 单聊消息，发送给指定的用户
            chatWebSocketHandler.sendMessageToUser(receiverId, message);
        }
    }
    @Override
    public void sendMessage(Long senderId, Long receiverId, Long groupId, String messageContent, int messageType, boolean isGroup) {
        ChatMessage message = new ChatMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setGroupId(groupId);
        message.setMessageType(messageType);
        message.setMessageContent(messageContent);
        message.setSentTime(LocalDateTime.now());
        message.setIsGroup(isGroup);
        message.setStatus(0);  // 默认消息未读

        // 保存消息
        chatMessageMapper.insert(message);
    }

    @Override
    public List<ChatMessageVO> getChatHistory(Long userId, Long friendId, Long groupId, int pageNo, int pageSize) {
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();

        if (groupId != null) {
            queryWrapper.eq(ChatMessage::getGroupId, groupId);
        } else {
            queryWrapper.eq(ChatMessage::getSenderId, userId)
                    .eq(ChatMessage::getReceiverId, friendId);
        }

        queryWrapper.orderByDesc(ChatMessage::getSentTime);
        queryWrapper.last("LIMIT " + (pageNo - 1) * pageSize + ", " + pageSize);

        List<ChatMessage> messages = chatMessageMapper.selectList(queryWrapper);

        List<ChatMessageVO> voList = new ArrayList<>();
        for (ChatMessage message : messages) {
            ChatMessageVO vo = new ChatMessageVO();
            vo.setSenderId(message.getSenderId());
            vo.setReceiverId(message.getReceiverId());
            vo.setGroupId(message.getGroupId());
            vo.setMessageContent(message.getMessageContent());
            vo.setMessageType(message.getMessageType());
            vo.setSentTime(message.getSentTime());
            vo.setIsGroup(message.getIsGroup());
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public Long createGroup(Long createdBy, String groupName) {
        ChatGroup group = new ChatGroup();
        Object myIdStr = StpUtil.getLoginId();
        Long myId= 0L;

        if (myIdStr != null) {
            myId = Long.valueOf(myIdStr.toString());
            // 现在 fromUserId 就是 Long 类型
        }
        group.setGroupName(groupName);
        group.setCreatedTime(LocalDateTime.now());


        group.setCreatedBy(createdBy);
group.setCreatedBy(myId);
        chatGroupMapper.insert(group);
        return group.getId();
    }

    @Override
    public void addGroupMember(Long groupId, Long userId) {
        ChatGroupMember member = new ChatGroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setJoinTime(LocalDateTime.now());

        chatGroupMemberMapper.insert(member);
    }
}
