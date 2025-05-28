package com.bruce.pojo;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private String type; // "text", "image", "video"
    private String fileUrl;

    // getter + setter
}
