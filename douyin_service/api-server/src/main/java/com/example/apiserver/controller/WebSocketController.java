package com.example.apiserver.controller;

import com.example.apiserver.entity.Message;
import com.example.apiserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 处理发送消息请求
     * @param messageRequest 消息请求
     * @return 发送的消息
     */
    @MessageMapping("/message/send")
    public Message sendMessage(@Payload MessageController.MessageRequest messageRequest) {
        // 保存消息到数据库
        Message sentMessage = messageService.sendMessage(
            messageRequest.getSenderId(), 
            messageRequest.getReceiverId(), 
            messageRequest.getContent()
        );
        
        // 发送消息给接收者
        messagingTemplate.convertAndSend(
            "/topic/messages/" + messageRequest.getReceiverId(), 
            sentMessage
        );
        
        // 发送消息给发送者（用于显示自己发送的消息）
        messagingTemplate.convertAndSend(
            "/topic/messages/" + messageRequest.getSenderId(), 
            sentMessage
        );
        
        return sentMessage;
    }
}
