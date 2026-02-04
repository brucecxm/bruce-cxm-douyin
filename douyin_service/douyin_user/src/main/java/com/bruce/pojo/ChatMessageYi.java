package com.bruce.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class ChatMessageYi {
    private String sender;
    private String receiver; // 一对一时必填
    private String content;
    private String type;     // text, image, video
    private String fileUrl;  // 可选
    private String fileName; // 文件名
    private Long fileSize;   // 文件大小
    private String mimeType; // 文件类型
    private Long timestamp;  // 时间戳
}