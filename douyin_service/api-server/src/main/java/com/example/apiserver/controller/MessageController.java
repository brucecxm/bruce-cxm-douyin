package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Message;
import com.example.apiserver.entity.User;
import com.example.apiserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public Response<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        try {
            System.out.println(messageRequest);
            Message sentMessage = messageService.sendMessage(
                messageRequest.getSenderId(), 
                messageRequest.getReceiverId(), 
                messageRequest.getContent()
            );
            return Response.success(sentMessage);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    // 消息请求DTO
    public static class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;

        // getter和setter方法
        public Long getSenderId() {
            return senderId;
        }

        public void setSenderId(Long senderId) {
            this.senderId = senderId;
        }

        public Long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @GetMapping("/user/{userId}")
    public Response<List<Object>> getChatList(@PathVariable Long userId) {
        try {
            List<Object> chatList = messageService.getChatList(userId);
            return Response.success(chatList);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
    
    @GetMapping("/chat-users/{userId}")
    public Response<List<User>> getChatUsers(@PathVariable Long userId) {
        try {
            List<User> chatUsers = messageService.getChatUsers(userId);
            return Response.success(chatUsers);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/unread/{userId}")
    public Response<List<Message>> getUnreadMessages(@PathVariable Long userId) {
        try {
            List<Message> messages = messageService.getUnreadMessages(userId);
            return Response.success(messages);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/chat")
    public Response<List<Message>> getChatMessages(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            List<Message> messages = messageService.getChatMessages(userId1, userId2);
            return Response.success(messages);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/read")
    public Response<Void> markMessageAsRead(@PathVariable Long id) {
        try {
            messageService.markMessageAsRead(id);
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
}
