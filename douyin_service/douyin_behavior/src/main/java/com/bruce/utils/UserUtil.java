package com.bruce.utils;

import com.bruce.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Hai
 * @date 2020/6/16 - 22:56
 * 用户工具类
 */
public class UserUtil {
  /**
   * 获取当前登录用户实体
   * @return
   */
  public static User getCurrentUser(){
    return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
