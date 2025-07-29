package com.bruce.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dto.PermissionDTO;
import com.bruce.entity.Permission;
import com.bruce.service.PermissionService;

import com.bruce.entity.ChatMessage;
import com.bruce.pojo.*;
import com.bruce.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/create")
    public R create(@RequestBody PermissionDTO dto) {
        Permission permission=new Permission();
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setDescription(dto.getDescription());
        permissionService.save(permission);
        return R.ok("权限创建成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody PermissionDTO dto) {
        // 校验ID是否存在
        if (dto.getId() == null) {
            return R.failed("权限ID不能为空");
        }

        // 查询现有实体
        Permission permission = permissionService.getById(dto.getId());
        if (permission == null) {
            return R.failed("权限不存在");
        }

        // 更新实体属性
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setDescription(dto.getDescription());

        // 执行更新
        boolean success = permissionService.updateById(permission);
        if (success) {
            return R.ok("权限修改成功");
        } else {
            return R.failed("权限修改失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return R.ok("权限删除成功");
    }

    @GetMapping("/list")
    public R<List<Permission>> list() {
        return R.ok(permissionService.list());
    }
}
