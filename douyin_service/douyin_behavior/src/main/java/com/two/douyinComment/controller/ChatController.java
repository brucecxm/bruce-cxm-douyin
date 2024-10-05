package com.two.douyinComment.controller;

import com.two.douyinComment.entity.User;
import com.two.douyinComment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @author Hai
 * @date 2020/6/16 - 21:32
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
  @Autowired
  UserService userService;

  @GetMapping("/users")
  public List<User> getUsersWithoutCurrentUser(){
    return userService.getUsersWithoutCurrentUser();
  }
}
