//package com.bruce.service;
//
//import com.bruce.pojo.LoginDto;
//import com.bruce.pojo.MySysUserDetails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//public class SysUserService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public String login(LoginDto loginDto) {
//        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(usernamePassword);
//        MySysUserDetails mySysUserDetails = (MySysUserDetails) authenticate.getPrincipal();
//        return UUID.randomUUID().toString();
//    }
//}
