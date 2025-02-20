package com.bruce.controller;
import com.bruce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email" )
    public String sendEmail(String code,String email) {

        emailService.sendSimpleEmail(email, "仿抖音短视频--验证码", "您的验证码是"+code);

        return "Email sent successfully!";
    }
}
