package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Likeone)表实体类
 *
 * @author makejava
 * @since 2025-05-19 16:09:37
 */
@SuppressWarnings("serial")
@Data
public class Likeone extends Model<Likeone> {
    
    private Long likeUserid;
    
    private Long likeVideoid;
    
    private Long share;
    
    private Long like;
    
    private Long collect;
    
    private Long comment;
    
    private Long follow;
    
    private Date createTime;


}

