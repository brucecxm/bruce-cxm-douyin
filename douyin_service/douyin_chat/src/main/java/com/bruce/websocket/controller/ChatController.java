package com.bruce.websocket.controller;

import com.bruce.websocket.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // 处理发送消息的请求
    // 当客户端向 /chat.sendMessage 发送消息时，这个方法会被调用
    // 方法返回的消息会被发送到 /topic/public 频道
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 返回的消息会广播到所有订阅了 /topic/public 频道的客户端
        return chatMessage;
    }

    // 处理添加用户的请求
    // 当客户端向 /chat.addUser 发送消息时，这个方法会被调用
    // 方法返回的消息会被发送到 /topic/public 频道
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage) {
        // 设置消息类型为 "JOIN"，表示用户加入了聊天
        chatMessage.setType("JOIN");
        // 返回的消息会广播到所有订阅了 /topic/public 频道的客户端
        return chatMessage;
    }
}
