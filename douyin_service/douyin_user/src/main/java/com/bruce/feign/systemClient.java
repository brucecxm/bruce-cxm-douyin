package com.bruce.feign;


import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

@FeignClient("DouyinAdminService")
public interface systemClient {

    @GetMapping("/dcSystem/{id}")
    R findById(@PathVariable("id") Serializable id);


}
