package com.example.apiserver.repository;

import com.example.apiserver.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * UserRepository Feign 客户端
 * 调用 douyin_user 服务中的 UserRepositoryController 接口
 * 直接调用服务时，使用服务内部的路径，不需要网关路由前缀
 */
@Repository
@FeignClient(name = "DouyinUserService", path = "/api/repository/user")
public interface UserRepository {

    /**
     * 保存用户信息
     * 
     * @param user 用户信息
     * @return User 保存后的用户信息
     */
    @PostMapping("/save")
    User save(@RequestBody User user);

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findById/{id}")
    Optional<User> findById(@PathVariable("id") Long id);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findByUsername")
    Optional<User> findByUsername(@RequestParam("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findByEmail")
    Optional<User> findByEmail(@RequestParam("email") String email);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return Boolean 是否存在
     */
    @GetMapping("/existsByUsername")
    Boolean existsByUsername(@RequestParam("username") String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return Boolean 是否存在
     */
    @GetMapping("/existsByEmail")
    Boolean existsByEmail(@RequestParam("email") String email);
}