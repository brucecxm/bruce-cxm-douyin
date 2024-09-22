//package com.bruce.pojo;
//
//import lombok.Data;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.AuthorityUtils;
////import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//@Data
//public class User implements UserDetails {
//    private String username;
//    private String password;
//    private boolean accountNonExpired;
//    private boolean accountNonLocked;
//    private boolean credentialsNonExpired;
//    private boolean enabled;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//        this.accountNonExpired = true;
//        this.accountNonLocked = true;
//        this.credentialsNonExpired = true;
//        this.enabled = true;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return accountNonExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return accountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return credentialsNonExpired;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//}
