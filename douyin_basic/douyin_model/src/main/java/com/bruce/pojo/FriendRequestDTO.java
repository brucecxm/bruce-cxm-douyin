package com.bruce.pojo;

import lombok.Data;

@Data
public class FriendRequestDTO {
    private Long toUserId;
    private String verifyMsg;
}