package com.bruce.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dto.RoleDTO;
import com.bruce.dto.RolePermissionDTO;
import com.bruce.entity.Permission;
import com.bruce.entity.Role;
import com.bruce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public R createRole(@RequestBody RoleDTO dto) {
        roleService.createRole(dto);
        return R.ok("角色创建成功");
    }

    @PostMapping("/update")
    public R updateRole(@RequestBody RoleDTO dto) {
        // 校验ID是否存在
        if (dto.getId() == null) {
            return R.failed("角色ID不能为空");
        }

        // 查询现有实体
        Role role = roleService.getById(dto.getId());
        if (role == null) {
            return R.failed("角色不存在");
        }

        // 更新实体属性
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setDescription(dto.getDescription());

        // 执行更新
        boolean success = roleService.updateById(role);
        if (success) {
            return R.ok("角色更新成功");
        } else {
            return R.failed("角色更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public R deleteRole(@PathVariable Long id) {
        // 校验ID是否存在
        if (id == null) {
            return R.failed("角色ID不能为空");
        }

        // 查询现有实体
        Role role = roleService.getById(id);
        if (role == null) {
            return R.failed("角色不存在");
        }

        // 执行删除（建议使用逻辑删除）
        boolean success = roleService.removeById(id);
        if (success) {
            return R.ok("角色删除成功");
        } else {
            return R.failed("角色删除失败");
        }
    }

    @GetMapping("/list")
    public R<List<Role>> listRoles() {
        return R.ok(roleService.list());
    }

    @PostMapping("/assignPermissions")
    public R assignPermissions(@RequestBody RolePermissionDTO dto) {
        roleService.assignPermissions(dto.getRoleId(), dto.getPermissionIds());
        return R.ok("权限分配成功");
    }

    @GetMapping("/permissions/{roleId}")
    public R<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        return R.ok(roleService.getPermissionsByRoleId(roleId));
    }
}
