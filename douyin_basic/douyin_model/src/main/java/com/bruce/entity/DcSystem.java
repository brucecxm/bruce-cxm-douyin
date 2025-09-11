package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (DcSystem)表实体类
 *
 * @author makejava
 * @since 2025-07-03 14:18:00
 */
@SuppressWarnings("serial")
@Data
public class DcSystem extends Model<DcSystem> {

    private String sysName;

    private String sysValue;

    private Long id;

    private Date createTime;

    private Date updateTime;

    }

