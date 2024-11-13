package com.bruce.user.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class User {
    @NotNull
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;
    @NotEmpty
    @Email
    private String email;
    private String userPic;
    private String backImg;
    private String city;
    private String jieshao;
    private String age;
    private String school;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
