package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserDao;
import com.bruce.dto.UserInfoRequestDTO;
import com.bruce.dto.UserInfoResponseDTO;
import com.bruce.entity.User;
import com.bruce.entity.Salt;
import com.bruce.service.UserService;
import com.bruce.service.SaltService;
import com.bruce.service.strategy.LoginStrategy;
import com.bruce.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2024-11-19 11:42:31
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> searchUsersByInfo(String keyword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getEmail, keyword)
                .or()
                .like(User::getPhone, keyword)
                .or()
                .eq(User::getId, keyword);
        return userDao.selectList(wrapper);
    }

    @Override
    public List<Long> listAllUserId() {
        // 使用MyBatis-Plus的lambda查询，只查询ID字段
        return this.listObjs(new LambdaQueryWrapper<User>(), o -> Long.valueOf(o.toString()));
    }

    @Override
    public List<Long> getHotUserIds(int limit) {
        return null;
    }

    @Override
    public User getById(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        return userDao.selectOne(queryWrapper);
    }


    /**
     * 重写 根据ID更新用户
     */
    @Override
    public boolean updateById(User user) {
        // 你可以在这里写自定义逻辑
        // 示例：自动更新更新时间
//        if (user != null) {
//            user.onUpdate(); // 调用实体类里的更新时间方法
//        }

        // 调用 MyBatis-Plus 原生的更新方法
        return super.updateById(user);
    }
    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        return userDao.selectOne(wrapper);
    }

    @Autowired
    private SaltService saltService;

    @Override
    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        // 根据用户ID获取盐值
        Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, user.getId()));
        if (salt == null) {
            return false;
        }
        // 使用注册时相同的算法进行双重哈希加盐加密
        try {
            // 第一次哈希，使用SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest((password + salt.getSalt()).getBytes());
            // 第二次哈希，使用MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secondHash = md5.digest(firstHash);
            // 转换为Base64编码
            String encryptedPassword = Base64.getEncoder().encodeToString(secondHash);
            // 验证密码是否正确
            return encryptedPassword.equals(user.getPassword());
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userDao.updateById(user);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        // 根据用户ID获取盐值
        Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, userId));
        if (salt == null) {
            return false;
        }
        // 使用相同的算法进行双重哈希加盐加密
        try {
            // 第一次哈希，使用SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest((newPassword + salt.getSalt()).getBytes());
            // 第二次哈希，使用MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secondHash = md5.digest(firstHash);
            // 转换为Base64编码
            String encryptedPassword = Base64.getEncoder().encodeToString(secondHash);
            // 加密新密码并更新
            user.setPassword(encryptedPassword);
            return updateById(user);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Autowired
    private com.bruce.service.strategy.LoginStrategyFactory loginStrategyFactory;

    @Autowired
    private com.bruce.utils.AuthUtils authUtils;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    private org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate;

    @Autowired
    private com.bruce.service.strategy.CachingStrategyFactory cachingStrategyFactory;

    @Autowired
    private com.bruce.service.VideoFeignService videoFeignService;

    @Override
    public java.util.Map<String, Object> login(com.bruce.dto.LoginRequestDTO loginRequestDTO) {
        try {
            // 获取登录策略
           LoginStrategy strategy = loginStrategyFactory.getStrategy(loginRequestDTO.getLoginType());
            // 执行登录
            User user = strategy.login(loginRequestDTO);

            // 更新用户最后登录时间
            updateLastLoginTime(user.getId());

            // 清除旧的权限缓存，确保获取最新权限
            authUtils.clearPermissionCache(user.getId());

            // 使用AuthUtils获取权限和角色列表
            List<String> roleList = authUtils.getRoleList(user.getId());
            List<String> permissionList = authUtils.getPermissionList(user.getId());

            // ===== 获取 token 和用户信息 =====
            cn.dev33.satoken.stp.StpUtil.login(user.getId());
            String token = cn.dev33.satoken.stp.StpUtil.getTokenValue();
            Long userId = Long.parseLong(cn.dev33.satoken.stp.StpUtil.getLoginId().toString());
            User userInfo = null;
            String userInfoKey = userId + com.bruce.dict.Constant.User._USER_INFO;
            // 先从 Redis 中查询用户信息
            try {
                String userInfoJson = stringRedisTemplate.opsForValue().get(userInfoKey);
                if (userInfoJson != null) {
                    // Redis 中存在，直接反序列化为对象
                    userInfo = objectMapper.readValue(userInfoJson, User.class);
                } else {
                    // Redis 中不存在，从数据库查询
                    userInfo = getById(userId);
                    if (userInfo != null) {
                        // 将查询到的用户信息存入 Redis (10分钟)
                        userInfoJson = objectMapper.writeValueAsString(userInfo);
                        stringRedisTemplate.opsForValue().set(userInfoKey, userInfoJson, 10, java.util.concurrent.TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                // Redis 操作失败，从数据库查询
                userInfo = getById(userId);
            }

            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put(com.bruce.dict.Constant.User.TOKEN, token);
            response.put(com.bruce.dict.Constant.User.USER_INFO, userInfo);
            response.put(com.bruce.dict.Constant.User.ROLE_LIST, roleList);
            response.put(com.bruce.dict.Constant.User.PERMISSION_LIST, permissionList);
            return response;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("登录失败，请稍后重试", e);
        }
    }

    @Override
    public com.bruce.dto.UserInfoResponseDTO getUserInfo(com.bruce.dto.UserInfoRequestDTO requestDTO) {
                try {
                    // 获取缓存策略
                    com.bruce.service.strategy.CachingStrategy strategy = cachingStrategyFactory.getStrategy(requestDTO.getType());

            // 使用策略获取用户信息
            com.bruce.entity.UserInfo userInfo = strategy.getUserInfo(
                requestDTO.getUserId(),
                requestDTO.getPage(),
                requestDTO.getPageSize()
            );

            if (userInfo == null) {
                return null;
            }

            // 转换为响应DTO
            com.bruce.dto.UserInfoResponseDTO responseDTO = new com.bruce.dto.UserInfoResponseDTO();
            // 复制基本信息
            responseDTO.setId(userInfo.getId());
            responseDTO.setNickname(userInfo.getNickname());
            responseDTO.setUsername(userInfo.getUsername());
            responseDTO.setAvatar(userInfo.getAvatar());
            responseDTO.setUserPic(userInfo.getUserPic());
            responseDTO.setBackImg(userInfo.getBackImg());
            responseDTO.setCity(userInfo.getCity());
            responseDTO.setMyIntro(userInfo.getMyIntro());
            responseDTO.setJieshao(userInfo.getMyIntro()); // 兼容前端字段
            responseDTO.setAge(userInfo.getAge());
            responseDTO.setSchool(userInfo.getSchool());
            responseDTO.setGender(userInfo.getGender());

            // 复制统计信息
            responseDTO.setFansCount(userInfo.getFansCount());
            responseDTO.setFollowCount(userInfo.getFollowCount());
            responseDTO.setMutualFollowCount(userInfo.getMutualFollowCount());
            responseDTO.setFollowers(userInfo.getFollowers());
            responseDTO.setFollowing(userInfo.getFollowing());
            responseDTO.setMutualFollow(userInfo.getMutualFollow());

            // 处理视频信息
            if (userInfo.getVideoPage() != null) {
                // 转换视频列表为DTO
                java.util.List<com.bruce.dto.VideoDTO> videoDTOs = new java.util.ArrayList<>();
                for (com.bruce.entity.Video video : userInfo.getVideoPage().getRecords()) {
                    com.bruce.dto.VideoDTO videoDTO = new com.bruce.dto.VideoDTO();
                    videoDTO.setId(video.getVideoId());
                    videoDTO.setVideoId(video.getVideoId());
                    videoDTO.setUserId(video.getUserId());
                    videoDTO.setMusicId(video.getMusicId());
                    videoDTO.setUrl(video.getUrl());
                    videoDTO.setContent(video.getContent());
                    videoDTO.setTitle(video.getTitle());
                    videoDTO.setCover(video.getCover());
                    videoDTO.setType(video.getType());
                    videoDTO.setStatus(video.getStatus());
                    videoDTOs.add(videoDTO);
                }

                // 创建分页对象
                com.baomidou.mybatisplus.core.metadata.IPage<com.bruce.dto.VideoDTO> videoPageDTO = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
                videoPageDTO.setRecords(videoDTOs);
                videoPageDTO.setTotal(userInfo.getVideoPage().getTotal());
                videoPageDTO.setSize(userInfo.getVideoPage().getSize());
                videoPageDTO.setCurrent(userInfo.getVideoPage().getCurrent());

                responseDTO.setVideoPage(videoPageDTO);
                responseDTO.setTotal((int) userInfo.getVideoPage().getTotal());
            }

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }

    @Override
    public boolean updateUserInfo(User user, Long userId) {
            // 确保只能更新自己的信息
        if (user.getId() == null || !user.getId().equals(userId)) {
            throw new IllegalArgumentException("无权修改其他用户信息");
        }

        // 更新用户信息
        boolean success = this.updateById(user);
        if (success) {
            // 清除 Redis 缓存
            String cacheKey = "user_info:" + userId;
            try {
                stringRedisTemplate.delete(cacheKey);
            } catch (Exception e) {
                // Redis 操作失败不应影响主流程
            }
        }
        return success;
    }

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<User> getUserList(Integer page, Integer pageSize) {
        // 使用MyBatis Plus的分页功能查询所有用户
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> pageQuery = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        return this.page(pageQuery);
    }

    @Override
    public User register(String username, String password, boolean isCheck, String verificationCode) {
        // 参数校验
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        // 验证码校验
        if (isCheck) {
            if (verificationCode == null || verificationCode.isEmpty()) {
                throw new IllegalArgumentException("验证码不能为空");
            }
            String storedCode = stringRedisTemplate.opsForValue()
                    .get(com.bruce.dict.Constant.User.EMAIL_CODE_PREFIX + username);
            if (storedCode == null || !verificationCode.equalsIgnoreCase(storedCode)) {
                throw new IllegalArgumentException("验证码错误");
            }
        }

        // 查询是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User existingUser = this.getOne(queryWrapper);

        if (existingUser != null) {
            throw new com.bruce.handlar.MyCustomException(400, com.bruce.dict.Constant.Error.USER_REPEAT);
        }

        // 生成盐值并保存
        String saltStr = generateSalt(16);
        com.bruce.entity.Salt salt = new com.bruce.entity.Salt();
        salt.setUserId(0L); // 临时ID，后续会更新
        salt.setSalt(saltStr);
        saltService.save(salt);

        // 加密密码
        String encryptedPassword = saltedDoubleHashPassword(password, saltStr);

        // 设置用户基本信息
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setNickname(com.bruce.dict.Constant.Chinese.NEW_USER + System.currentTimeMillis());
        user.setRoleId(com.bruce.dict.Constant.Role.USER);

        // 保存用户
        this.save(user);

        // 更新盐值的用户ID
        salt.setUserId(user.getId());
        saltService.updateById(salt);

        return user;
    }

    @Override
    public boolean updateInfo(Long userId, String nickname, String avatar, String phone, String gender) {
        // 验证用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }

        // 更新用户信息
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (gender != null) {
            user.setGender(gender);
        }

        boolean success = this.updateById(user);
        if (success) {
            // 清除Redis缓存
            String userInfoKey = userId + com.bruce.dict.Constant.User._USER_INFO;
            stringRedisTemplate.delete(userInfoKey);
        }
        return success;
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword, String verificationCode) {
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }

        // 验证原密码或验证码
        if (verificationCode != null) {
            // 验证码方式
            // 这里简化处理，实际应验证验证码
        } else {
            // 原密码方式
            if (oldPassword == null) {
                throw new IllegalArgumentException("原密码不能为空");
            }

            // 获取用户盐值并验证密码
            com.bruce.entity.Salt salt = saltService.getOne(new LambdaQueryWrapper<com.bruce.entity.Salt>().eq(com.bruce.entity.Salt::getUserId, userId));
            if (salt == null) {
                return false;
            }

            String encryptedOldPassword = saltedDoubleHashPassword(oldPassword, salt.getSalt());
            if (!encryptedOldPassword.equals(user.getPassword())) {
                throw new IllegalArgumentException("原密码错误");
            }
        }

        // 更新密码
        String newSalt = generateSalt(16);
        String encryptedNewPassword = saltedDoubleHashPassword(newPassword, newSalt);

        user.setPassword(encryptedNewPassword);
        boolean success = this.updateById(user);

        // 更新盐值
        if (success) {
            com.bruce.entity.Salt salt = saltService.getOne(new LambdaQueryWrapper<com.bruce.entity.Salt>().eq(com.bruce.entity.Salt::getUserId, userId));
            if (salt != null) {
                salt.setSalt(newSalt);
                saltService.updateById(salt);
            }
        }

        return success;
    }

    @Autowired
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Override
    public java.util.Map<String, Object> getVerificationCode(String uuid, String userName) {
        if (uuid == null || uuid.isEmpty()) {
            uuid = String.valueOf(System.currentTimeMillis());
        }
        // 创建验证码对象（设置图片宽高和验证码字符数）
        com.wf.captcha.SpecCaptcha captcha = new com.wf.captcha.SpecCaptcha(120, 40, 5);
        captcha.setCharType(com.wf.captcha.base.Captcha.TYPE_DEFAULT);
        String intervalKey = "code:interval:" + userName;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(intervalKey))) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }
        stringRedisTemplate.opsForValue().set(intervalKey, "1", 60, java.util.concurrent.TimeUnit.SECONDS);

        // 存入 Redis，过期时间 3 分钟
        stringRedisTemplate.opsForValue().set("CAPTCHA_" + uuid + userName, captcha.text(), 3, java.util.concurrent.TimeUnit.MINUTES);

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("uuid", uuid);
        result.put("captcha", captcha);
        return result;
    }

    @Override
    public void getEmailVerificationCode(String email) {
        // 生成验证码
        String code = String.valueOf(new java.util.Random().nextInt(899999) + 100000);
        // 存入 Redis，有效期 5 分钟
        stringRedisTemplate.opsForValue().set("email:code:" + email, code, 5, java.util.concurrent.TimeUnit.MINUTES);
        // 构造消息
        java.util.Map<String, String> message = new java.util.HashMap<>();
        message.put("email", email);
        message.put("code", code);
        // 发送到队列
        rabbitTemplate.convertAndSend("email.queue", message);
    }

    @Override
    public com.bruce.dto.UserStatusResponseDTO getUserStatus(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 模拟返回正常状态
        return new com.bruce.dto.UserStatusResponseDTO(1, "");
    }

    @Override
    public UserInfoResponseDTO getUserInfoWithLoginCheck(UserInfoRequestDTO requestDTO, Long currentUserId) {
        // 如果未提供userId，使用当前登录用户ID
        if (requestDTO.getUserId() == null) {
            requestDTO.setUserId(currentUserId);
        }

        // 验证userId格式
        if (requestDTO.getUserId() <= 0) {
            throw new IllegalArgumentException("用户ID格式错误，请输入有效的数字ID");
        }

        return getUserInfo(requestDTO);
    }

    @Override
    public boolean updateInfoWithLoginCheck(com.bruce.dto.UpdateInfoRequestDTO request, Long currentUserId) {
        Long userId = request.getUserId();
        // 如果未指定userId，使用当前登录用户
        if (userId == null) {
            userId = currentUserId;
        }

        return updateInfo(userId, request.getNickname(), request.getAvatar(), request.getPhone(), request.getGender());
    }

    @Override
    public boolean changePasswordWithLoginCheck(com.bruce.dto.ChangePasswordRequestDTO request, Long currentUserId) {
        Long userId = request.getUserId();
        // 如果未指定userId，使用当前登录用户
        if (userId == null) {
            userId = currentUserId;
        }

        return changePassword(userId, request.getOldPassword(), request.getNewPassword(), request.getVerificationCode());
    }

    // 生成一个随机盐值
    private String generateSalt(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder salt = new StringBuilder(length);
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < length; i++) {
            salt.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return salt.toString();
    }

    // 双重哈希加盐加密
    private String saltedDoubleHashPassword(String password, String salt) {
        try {
            // 第一次哈希，使用SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest((password + salt).getBytes());
            // 第二次哈希，使用MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secondHash = md5.digest(firstHash);
            // 返回Base64编码的加密密码
            return Base64.getEncoder().encodeToString(secondHash);
        } catch (NoSuchAlgorithmException e) {
            throw new com.bruce.handlar.MyCustomException(400, com.bruce.dict.Constant.Error.HASH_TRANSFORM_ERROR);
        }
    }
}
