package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor // 生成无参构造方法
@RequiredArgsConstructor // 生成必需参数的构造方法
public class RolePermission extends Model<RolePermission> {

    private Long id;

    @NonNull // 标记为必需参数
    private Long roleId;

    @NonNull // 标记为必需参数
    private Long permissionId;
}
