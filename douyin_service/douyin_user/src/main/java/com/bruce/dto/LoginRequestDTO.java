package com.bruce.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String userName;
    private String password;
    private String isCheck;
    private String verificationCode;
    private String emailVerificationCode;
    private String loginType;
}