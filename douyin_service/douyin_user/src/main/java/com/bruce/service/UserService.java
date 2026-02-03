package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.User;

import java.util.List;
import java.util.Map;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2024-11-19 11:42:31
 */
public interface UserService extends IService<User> {
    /**
     * 根据关键词搜索用户
     * @param keyword 搜索关键词
     * @return 用户列表
     */
    List<User> searchUsersByInfo(String keyword);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 验证用户密码
     * @param username 用户名
     * @param password 密码
     * @return 验证结果
     */
    boolean verifyPassword(String username, String password);

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 操作结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 获取所有用户ID列表
     * @return 用户ID列表
     */
    List<Long> listAllUserId();


    /**
     * 获取热门用户ID列表
     * @param limit 获取的用户数量限制
     * @return 热门用户ID列表
     */
    List<Long> getHotUserIds(int limit);

    /**
     * 用户登录
     * @param loginRequestDTO 登录请求DTO
     * @return 登录用户信息
     */
    Map<String, Object> login(com.bruce.dto.LoginRequestDTO loginRequestDTO);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User getById(Long userId);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否更新成功
     */
    boolean updateById(User user);
}

