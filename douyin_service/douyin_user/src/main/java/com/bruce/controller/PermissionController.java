package com.bruce.controller;

import com.bruce.dto.PermissionDTO;
import com.bruce.entity.Permission;
import com.bruce.pojo.Result;
import com.bruce.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/create")
    public Result create(@RequestBody PermissionDTO dto) {
        Permission permission=new Permission();
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setDescription(dto.getDescription());
        permissionService.save(permission);
        return Result.success("权限创建成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody PermissionDTO dto) {
        // 校验ID是否存在
        if (dto.getId() == null) {
            return Result.error("权限ID不能为空");
        }

        // 查询现有实体
        Permission permission = permissionService.getById(dto.getId());
        if (permission == null) {
            return Result.error("权限不存在");
        }

        // 更新实体属性
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setDescription(dto.getDescription());

        // 执行更新
        boolean success = permissionService.updateById(permission);
        if (success) {
            return Result.success("权限修改成功");
        } else {
            return Result.error("权限修改失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return Result.success("权限删除成功");
    }

    @GetMapping("/list")
    public Result<List<Permission>> list() {
        return Result.success(permissionService.list());
    }
}
