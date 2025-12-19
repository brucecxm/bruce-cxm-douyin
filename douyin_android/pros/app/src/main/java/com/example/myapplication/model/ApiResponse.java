package com.example.myapplication.model;

import java.util.List;
import java.util.Map;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    
    // 从data中提取的字段
    private String token;
    private User userInfo;
    private List<String> roleList;
    private List<String> permissionList;
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
        
        // 如果data是Map类型，提取其中的字段
        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            
            // 提取token
            if (dataMap.containsKey("token")) {
                this.token = (String) dataMap.get("token");
            }
            
            // 提取userInfo
            if (dataMap.containsKey("userInfo")) {
                // 这里需要注意：Gson会将userInfo解析为LinkedHashMap，需要进一步转换为User对象
                // 暂时先存储为null，后面需要修改Gson的解析方式
            }
            
            // 提取roleList和permissionList
            if (dataMap.containsKey("roleList")) {
                this.roleList = (List<String>) dataMap.get("roleList");
            }
            
            if (dataMap.containsKey("permissionList")) {
                this.permissionList = (List<String>) dataMap.get("permissionList");
            }
        }
    }
    
    // 判断请求是否成功
    public boolean isSuccess() {
        return code != null && code == 0;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public User getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
    
    public List<String> getRoleList() {
        return roleList;
    }
    
    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
    
    public List<String> getPermissionList() {
        return permissionList;
    }
    
    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }
}
