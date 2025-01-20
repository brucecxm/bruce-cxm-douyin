package com.bruce.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;

    // Getters and Setters
}
