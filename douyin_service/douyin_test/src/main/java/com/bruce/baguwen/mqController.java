package com.bruce.baguwen;

import com.bruce.dtos.Order;
import com.bruce.service.OrderoneService;
import com.bruce.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mq")
public class mqController {


    //todo 这里用的是mq的作用
    //todo 这里用的是mq的作用

    @GetMapping
    public void  getAllOrders() {

        /*
        * 1.异步发送（验证码 短信 邮件）
        * 2.mysql和redis或者es的数据同步
        * 3.分布式事务：
        * 4.削峰填谷
        * */




    }

}