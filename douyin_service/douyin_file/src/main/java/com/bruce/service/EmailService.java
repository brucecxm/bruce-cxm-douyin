//package com.bruce.service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text) {
//        try {
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text, true); // true表示支持HTML格式
//
//            emailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            // 处理邮件发送异常
//        }
//    }
//}
