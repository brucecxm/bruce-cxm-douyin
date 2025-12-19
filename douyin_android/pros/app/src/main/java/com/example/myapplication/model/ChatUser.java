package com.example.myapplication.model;

import java.io.Serializable;

/**
 * 聊天用户模型
 */
public class ChatUser implements Serializable {
    private String userId;
    private String userName;
    private String avatar;
    private String lastMessage;
    private String lastMessageTime;
    private int unreadCount;

    // 默认构造函数
    public ChatUser() {
    }

    // 带参数的构造函数
    public ChatUser(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Getter和Setter方法
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}