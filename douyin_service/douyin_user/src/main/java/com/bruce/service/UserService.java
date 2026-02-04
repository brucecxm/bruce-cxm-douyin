package com.bruce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.User;
import com.bruce.entity.UserInfo;
import com.bruce.dto.UserInfoRequestDTO;
import com.bruce.dto.UserInfoResponseDTO;

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
    
    /**
     * 根据请求DTO获取用户信息
     * @param requestDTO 用户信息请求DTO
     * @return 用户信息响应DTO
     */
    UserInfoResponseDTO getUserInfo(UserInfoRequestDTO requestDTO);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @param userId 当前登录用户ID
     * @return 更新结果
     */
    boolean updateUserInfo(User user, Long userId);

    /**
     * 获取用户列表（分页）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    IPage<User> getUserList(Integer page, Integer pageSize);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param isCheck 是否需要验证码
     * @param verificationCode 验证码
     * @return 注册用户信息
     */
    User register(String username, String password, boolean isCheck, String verificationCode);

    /**
     * 更新用户详细信息
     * @param userId 用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @param phone 手机号
     * @param gender 性别
     * @return 更新结果
     */
    boolean updateInfo(Long userId, String nickname, String avatar, String phone, String gender);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @param verificationCode 验证码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword, String verificationCode);

    /**
     * 获取图片验证码
     * @param uuid 唯一标识符
     * @param userName 用户名
     * @return 验证码信息
     */
    java.util.Map<String, Object> getVerificationCode(String uuid, String userName);

    /**
     * 获取邮箱验证码
     * @param email 邮箱地址
     * @return 验证码信息
     */
    void getEmailVerificationCode(String email);

    /**
     * 获取用户状态
     * @param userId 用户ID
     * @return 用户状态响应DTO
     */
    com.bruce.dto.UserStatusResponseDTO getUserStatus(Long userId);

    /**
     * 根据用户ID获取用户信息（带登录状态处理）
     * @param requestDTO 用户信息请求DTO
     * @param currentUserId 当前登录用户ID
     * @return 用户信息响应DTO
     */
    UserInfoResponseDTO getUserInfoWithLoginCheck(UserInfoRequestDTO requestDTO, Long currentUserId);

    /**
     * 更新用户信息（带登录状态处理）
     * @param request 更新信息请求DTO
     * @param currentUserId 当前登录用户ID
     * @return 更新结果
     */
    boolean updateInfoWithLoginCheck(com.bruce.dto.UpdateInfoRequestDTO request, Long currentUserId);

    /**
     * 修改密码（带登录状态处理）
     * @param request 修改密码请求DTO
     * @param currentUserId 当前登录用户ID
     * @return 修改结果
     */
    boolean changePasswordWithLoginCheck(com.bruce.dto.ChangePasswordRequestDTO request, Long currentUserId);
}

