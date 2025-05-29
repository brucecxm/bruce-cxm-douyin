package com.bruce.controller;

import com.bruce.entity.ChatMessage;
import com.bruce.pojo.*;
import com.bruce.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private  SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat") // 客户端发送的路径: /app/chat
    @SendTo("/topic/messages") // 广播路径: /topic/messages
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ChatMessage handleChat(@DestinationVariable String roomId, ChatMessage message) {
        return message;
    }





    // 一对一私聊消息处理
    @MessageMapping("/private-chat")
    public void privateChat(ChatMessageYi message, Principal principal) {
        System.out.println("当前发消息的是：" + principal.getName());
        System.out.println("准备发送给：" + message.getReceiver());
        // 给指定用户发送点对点消息，接收者浏览器要订阅 /user/{receiver}/queue/messages
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }



    // 发送消息接口
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessageRequest request) {
        chatService.sendMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getGroupId(),
                request.getMessageContent(),
                request.getMessageType(),
                request.isGroup()

        );
        System.out.println("ss");
        return ResponseEntity.ok("Message sent successfully");
    }

    // 获取聊天记录接口
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageVO>> getChatHistory(
            @RequestParam Long userId,
            @RequestParam(required = false) Long friendId,
            @RequestParam(required = false) Long groupId,
            @RequestParam int pageNo,
            @RequestParam int pageSize) {

        List<ChatMessageVO> chatHistory = chatService.getChatHistory(userId, friendId, groupId, pageNo, pageSize);
        return ResponseEntity.ok(chatHistory);
    }

    // 创建群聊接口
    @PostMapping("/group/create")
    public ResponseEntity<Long> createGroup(@RequestBody CreateGroupRequest request) {
        Long groupId = chatService.createGroup(request.getCreatedBy(), request.getGroupName());
        return ResponseEntity.ok(groupId);
    }

    // 添加群聊成员接口
    @PostMapping("/group/{groupId}/add")
    public ResponseEntity<String> addGroupMember(@PathVariable Long groupId, @RequestBody AddGroupMemberRequest request) {
        chatService.addGroupMember(groupId, request.getUserId());
        return ResponseEntity.ok("User added to group");
    }
}
