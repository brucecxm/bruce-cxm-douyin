package com.bruce.dto;

public class LogoutRequestDTO {
    private String action; // 操作类型：logout、logoutByDevice等
    private Object param; // 操作参数，根据action不同格式不同

    // getter和setter（必须，用于JSON反序列化）
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
