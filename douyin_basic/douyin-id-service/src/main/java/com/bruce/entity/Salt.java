package com.bruce.entity;
import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Salt)表实体类
 *
 * @author makejava
 * @since 2025-07-03 13:24:01
 */
@Data
@SuppressWarnings("serial")
public class Salt extends Model<Salt> {

    private String salt;

    private Integer statu;

    private long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

