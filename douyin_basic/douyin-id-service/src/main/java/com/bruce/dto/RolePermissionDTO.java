package com.bruce.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionDTO {
    private Long roleId;
    private List<Long> permissionIds;
}
