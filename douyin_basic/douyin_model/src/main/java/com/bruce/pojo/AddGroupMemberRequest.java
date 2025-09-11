package com.bruce.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddGroupMemberRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;  // 要添加的成员ID
}
