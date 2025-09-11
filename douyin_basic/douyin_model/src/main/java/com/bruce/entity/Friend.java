package com.bruce.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 好友关系实体类
 */
@TableName("friend") // 表明这是对应数据库中的 friend 表
@Data
public class Friend {

    @TableId
    private Long id; // 好友关系记录的ID

    private Long userId; // 当前用户ID
    private Long friendId; // 好友ID

    private Integer status; // 好友关系的状态，1表示已同意，0表示待处理
    private LocalDateTime createdTime; // 记录创建时间

    @TableField(exist = false) // 不映射到数据库字段
    private String remark; // 好友备注

}
