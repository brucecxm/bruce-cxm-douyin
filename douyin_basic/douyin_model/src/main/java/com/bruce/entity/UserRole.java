package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * (UserRole)表实体类
 *
 * @author makejava
 * @since 2025-07-29 20:01:02
 */
@Data
@NoArgsConstructor // 生成无参构造方法
@RequiredArgsConstructor // 生成必需参数的构造方法
public class UserRole extends Model<UserRole> {
    private Long id;
    @NonNull // 标记为必需参数
    private Long userId;
    @NonNull // 标记为必需参数
    private Long roleId;


    }

