package com.bruce.feign;

import com.bruce.entity.User;
import com.bruce.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "DouyinUserService", path = "/user")
public interface UserFeignClient {

    @GetMapping("/getById")
    Result<User> getUserById(@RequestParam("id") Long id);

    @GetMapping("/list")
    Object getUserList();
}
