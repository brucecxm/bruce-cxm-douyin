package com.bruce.service.strategy;

import com.bruce.dto.LoginRequestDTO;
import com.bruce.entity.User;

public interface LoginStrategy {
    User login(LoginRequestDTO requestDTO);
}