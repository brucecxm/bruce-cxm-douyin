package com.example.apiserver.service;

import com.example.apiserver.entity.Message;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.MessageRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发送消息
     *
     * @param senderId   发送者ID
     * @param receiverId 接收者ID
     * @param content    消息内容
     * @return 发送的消息
     */
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        // 获取发送者和接收者
        Map<String, Object> senderMap = userRepository.findById(senderId);
        if (senderMap == null || senderMap.isEmpty()) {
            throw new RuntimeException("发送者不存在");
        }
        User sender = UserConverter.convertToUser(senderMap);

        Map<String, Object> receiverMap = userRepository.findById(receiverId);
        if (receiverMap == null || receiverMap.isEmpty()) {
            throw new RuntimeException("接收者不存在");
        }
        User receiver = UserConverter.convertToUser(receiverMap);

        // 创建消息对象
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setIsRead(false);

        // 保存消息
        return messageRepository.save(message);
    }

    /**
     * 获取两个用户之间的聊天记录
     *
     * @param userId1 用户1的ID
     * @param userId2 用户2的ID
     * @return 聊天记录列表
     */
    public List<Message> getChatMessages(Long userId1, Long userId2) {
        // 获取两个用户
        Map<String, Object> user1Map = userRepository.findById(userId1);
        if (user1Map == null || user1Map.isEmpty()) {
            throw new RuntimeException("用户1不存在");
        }
        User user1 = UserConverter.convertToUser(user1Map);

        Map<String, Object> user2Map = userRepository.findById(userId2);
        if (user2Map == null || user2Map.isEmpty()) {
            throw new RuntimeException("用户2不存在");
        }
        User user2 = UserConverter.convertToUser(user2Map);

        // 获取聊天记录
        return messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtAsc(user1, user2, user2,
                user1);
    }

    /**
     * 获取用户的未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public long getUnreadMessageCount(Long userId) {
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    /**
     * 获取用户与特定用户之间的未读消息数量
     *
     * @param senderId   发送者ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    public long getUnreadMessageCountBySender(Long senderId, Long receiverId) {
        Map<String, Object> senderMap = userRepository.findById(senderId);
        if (senderMap == null || senderMap.isEmpty()) {
            throw new RuntimeException("发送者不存在");
        }
        User sender = UserConverter.convertToUser(senderMap);

        Map<String, Object> receiverMap = userRepository.findById(receiverId);
        if (receiverMap == null || receiverMap.isEmpty()) {
            throw new RuntimeException("接收者不存在");
        }
        User receiver = UserConverter.convertToUser(receiverMap);

        return messageRepository.countBySenderAndReceiverAndIsReadFalse(sender, receiver);
    }

    /**
     * 标记消息为已读
     *
     * @param senderId   发送者ID
     * @param receiverId 接收者ID
     */
    @Transactional
    public void markMessagesAsRead(Long senderId, Long receiverId) {
        Map<String, Object> senderMap = userRepository.findById(senderId);
        if (senderMap == null || senderMap.isEmpty()) {
            throw new RuntimeException("发送者不存在");
        }
        User sender = UserConverter.convertToUser(senderMap);

        Map<String, Object> receiverMap = userRepository.findById(receiverId);
        if (receiverMap == null || receiverMap.isEmpty()) {
            throw new RuntimeException("接收者不存在");
        }
        User receiver = UserConverter.convertToUser(receiverMap);

        // 将消息标记为已读
        messageRepository.updateBySenderAndReceiverAndIsReadFalse(sender, receiver);
    }

    /**
     * 获取与用户有过聊天记录的所有用户
     *
     * @param userId 用户ID
     * @return 用户列表
     */
    public List<User> getChatUsers(Long userId) {
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        return messageRepository.findChatUsersByUserId(user);
    }

    /**
     * 获取用户的聊天列表（包含最新消息和未读数量）
     *
     * @param userId 用户ID
     * @return 聊天列表
     */
    public List<Object> getChatList(Long userId) {
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User currentUser = UserConverter.convertToUser(userMap);

        // 获取所有聊天用户
        List<User> chatUsers = messageRepository.findChatUsersByUserId(currentUser);

        // 构建聊天列表
        return chatUsers.stream().map(user -> {
            // 获取最新消息
            List<Message> latestMessages = messageRepository
                    .findTop1BySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(currentUser, user, user,
                            currentUser);
            Message latestMessage = latestMessages.isEmpty() ? null : latestMessages.get(0);
            // 获取未读消息数量
            long unreadCount = messageRepository.countBySenderAndReceiverAndIsReadFalse(user, currentUser);

            // 构建返回对象，使用HashMap避免null值问题
            java.util.Map<String, Object> chatItem = new java.util.HashMap<>();
            chatItem.put("id", user.getId());

            java.util.Map<String, Object> senderInfo = new java.util.HashMap<>();
            senderInfo.put("id", user.getId());
            senderInfo.put("nickname", user.getNickname());
            senderInfo.put("avatar", user.getAvatar());

            chatItem.put("sender", senderInfo);
            chatItem.put("preview", latestMessage != null ? latestMessage.getContent() : "");
            chatItem.put("time", latestMessage != null ? latestMessage.getCreatedAt() : null);
            chatItem.put("unreadCount", unreadCount);
            chatItem.put("updatedAt", latestMessage != null ? latestMessage.getCreatedAt() : null);

            return chatItem;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户的所有消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getMessagesByUserId(Long userId) {
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        // 注意：这里需要在MessageRepository中添加相应的方法
        // 为了简化，我们暂时返回空列表
        return new ArrayList<>();
    }

    /**
     * 获取用户的未读消息
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    public List<Message> getUnreadMessages(Long userId) {
        // 这里可以根据需求实现
        // 为了简化，我们暂时返回空列表
        return new ArrayList<>();
    }

    /**
     * 标记单条消息为已读
     *
     * @param messageId 消息ID
     */
    @Transactional
    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setIsRead(true);
        messageRepository.save(message);
    }

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     */
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
