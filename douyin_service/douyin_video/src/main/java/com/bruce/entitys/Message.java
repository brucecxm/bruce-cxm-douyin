package com.bruce.entitys;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    // 消息类型
    private String type;

    // 用户名
    private String username;

    // 消息内容
    private String content;

    // 礼物ID
    private Integer giftId;

    // 礼物名称
    private String giftName;

    // 礼物价格
    private Integer price;

    // 观众数量
    private Integer count;

    // 可以根据需要添加更多字段
}
