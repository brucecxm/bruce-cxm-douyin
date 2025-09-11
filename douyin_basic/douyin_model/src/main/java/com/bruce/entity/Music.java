package com.bruce.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * (Music)表实体类
 *
 * @author makejava
 * @since 2024-06-25 18:10:39
 */
@SuppressWarnings("serial")
@Data
public class Music  {
    
    private String musicAvatar;
    
    private String musicName;
    @TableId(value = "id")
    private int id;



    }

