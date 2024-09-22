//package com.bruce.pojo;
//
//import org.springframework.security.core.GrantedAuthority;
//
//import java.io.Serializable;
//import java.util.Collection;
//
//public interface UserDetails extends Serializable {
//    // 封装了权限信息
//    Collection<? extends GrantedAuthority> getAuthorities();
//    // 密码信息
//    String getPassword();
//    // 登录用户名
//    String getUsername();
//    // 帐户是否过期
//    boolean isAccountNonExpired();
//    // 帐户是否被冻结
//    boolean isAccountNonLocked();
//    // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
//    boolean isCredentialsNonExpired();
//    // 帐号是否可用
//    boolean isEnabled();
//}
