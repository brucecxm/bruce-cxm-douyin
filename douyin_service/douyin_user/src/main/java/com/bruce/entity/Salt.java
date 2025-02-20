package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Salt)表实体类
 *
 * @author makejava
 * @since 2025-02-20 15:01:08
 */
@SuppressWarnings("serial")
public class Salt extends Model<Salt> {
    
    private String username;
    
    private String salt;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}

