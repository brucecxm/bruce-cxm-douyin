package com.bruce.controller;

import com.bruce.entity.ChatLog;
import com.bruce.entity.ChatMessage;
import com.bruce.service.ChatLogService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class ChatController {
    @Resource
    private ChatLogService chatLogService;
    // 处理发送消息的请求
    // 当客户端向 /chat.sendMessage 发送消息时，这个方法会被调用
    // 方法返回的消息会被发送到 /topic/public 频道
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 返回的消息会广播到所有订阅了 /topic/public 频道的客户端
        return chatMessage;
    }



    // 处理发送消息的请求
    // 当客户端向 /chat.sendMessage 发送消息时，这个方法会被调用
    // 方法返回的消息会被发送到 /topic/public 频道
    @MessageMapping("/chat.sendMessage/{userId}/{friendId}")
    @SendTo("/topic/chat/{userId}/{friendId}")
    public ChatMessage sendMessageone(ChatMessage chatMessage) {
        // 返回的消息会广播到所有订阅了 /topic/public 频道的客户端
        return chatMessage;
    }

    // 处理添加用户的请求
    @MessageMapping("/chat.addUser/{userId}/{friendId}")
    @SendTo("/topic/chat/{userId}/{friendId}")
    public ChatMessage addUser(@DestinationVariable String userId,
                               @DestinationVariable String friendId,
                               ChatMessage chatMessage) {
        // 设置消息类型为 "JOIN"，表示用户加入了聊天
        // 返回的消息会广播到所有订阅了 /topic/chat/{userId}/{friendId} 频道的客户端
        ChatLog chatLog=new ChatLog();
        chatLog.setUserId(userId);
        chatLog.setFriendId(friendId);
        this.chatLogService.save(chatLog);

        return chatMessage;
    }



    @MessageMapping("/chat.sendMessagedan")
    @SendTo("/topic/public")
    public ChatMessage sendMessagedan(ChatMessage chatMessage) {
        // 返回的消息会广播到所有订阅了 /topic/public 频道的客户端
        return chatMessage;
    }


}
