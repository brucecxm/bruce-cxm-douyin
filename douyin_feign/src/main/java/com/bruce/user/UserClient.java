package com.bruce.user;

import com.bruce.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("DouyinUserService")
public interface UserClient {
    @GetMapping("/userfeign/avatar")
    public User avatar(@RequestParam int authId , @RequestHeader("Authorization") String token);

}
