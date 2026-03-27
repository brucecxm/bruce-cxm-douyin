package com.bruce.service.strategy.login;

import com.bruce.dto.LoginRequestDTO;
import com.bruce.entity.User;

public interface LoginStrategy {
    User login(LoginRequestDTO requestDTO);
}
