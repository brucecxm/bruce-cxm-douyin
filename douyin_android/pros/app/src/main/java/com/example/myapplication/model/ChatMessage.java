package com.example.myapplication.model;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String sender;
    private String receiver;
    private String content;
    private String type;
    private String fileUrl;

    public ChatMessage() {
    }

    public ChatMessage(String sender, String receiver, String content, String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}