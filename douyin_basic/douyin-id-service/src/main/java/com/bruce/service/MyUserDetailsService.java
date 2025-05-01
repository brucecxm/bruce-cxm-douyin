//package com.bruce.service;
//
//import com.bruce.pojo.User;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//@Component
//public class MyUserDetailsService implements UserDetailsService {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.info("用户的用户名: {}", username);
//        // TODO 根据用户名，查找到对应的密码，与权限
//
//        // 将密码加密
//        String password = passwordEncoder.encode("123456");
//
//        // 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
//        User user = new User(username, password,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//        return user;
//    }
//}