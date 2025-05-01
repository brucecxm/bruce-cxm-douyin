package com.bruce.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("secruy")  // 指定数据库表名
public class SysUser {

    @TableId
    private Integer id;
    private String username;
    private String password;
}
