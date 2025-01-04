package com.bruce.listen;
import com.bruce.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @Autowired
    private EmailService emailService;

    //发送验证码到邮箱
    @RabbitListener(queues = "YanZhengQueue")
    public void receiveMessage(String message) {
        System.out.println("Received Message: " + message);
        emailService.sendSimpleEmail("2945090920@qq.com", "Test Subject", message);

    }
}
