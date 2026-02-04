package com.bruce.dto;

import lombok.Data;

@Data
public class UserInfoRequestDTO {
    private Long userId;
    private Integer page;
    private Integer pageSize;
    private String type; // bloom, empty, lock
    private boolean isRedis; // 是否直接从数据库查询
}
