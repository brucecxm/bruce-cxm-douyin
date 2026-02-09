package com.bruce.service;

import com.bruce.feign.UserFeignClient;
import com.bruce.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFeignService {

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 通过Feign调用douyin_user微服务，根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    public Object getUserById(Long id) {
        Result<?> result = userFeignClient.getUserById(id);
        return result.getCode() == 0 ? result.getData() : null;
    }

    /**
     * 通过Feign调用douyin_user微服务，获取用户列表
     * @return 用户列表
     */
    public Object getUserList() {
        Result<?> result = userFeignClient.getUserList();
        return result.getCode() == 0 ? result.getData() : null;
    }
}
