package com.bruce.dto;

public class UserStatusResponseDTO {
    private Integer status; // 1-正常，2-冻结，3-禁用
    private String reason; // 状态原因

    public UserStatusResponseDTO(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    // getter
    public Integer getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
