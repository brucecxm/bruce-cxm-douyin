package com.bruce.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String permissionName;
    private String permissionCode;
    private String description;
}
