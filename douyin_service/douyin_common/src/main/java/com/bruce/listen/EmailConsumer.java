package com.bruce.listen;

import com.bruce.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailConsumer {
    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "email.queue", concurrency = "3")
    public void handleMessage(Map<String, String> msg) {
        String email = msg.get("email");
        String code = msg.get("code");

        // 伪代码 - 实际调用 JavaMail 发送
        System.out.println("【发送验证码】To: " + email + "，Code: " + code);
        emailService.sendSimpleEmail(email, "Test Subject", code);

    }
}
