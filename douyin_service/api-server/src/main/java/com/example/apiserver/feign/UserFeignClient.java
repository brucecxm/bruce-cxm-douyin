package com.example.apiserver.feign;

import com.example.apiserver.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "DouyinUserService", contextId = "userFeignClient")
public interface UserFeignClient {

    @PostMapping("/user/register")
    Response<?> register(@RequestParam Map<String, Object> params);

    @PostMapping("/user/doLogin")
    Response<?> doLogin(@RequestParam Map<String, String> params);
}
