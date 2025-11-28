package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dict.Constant;
import com.bruce.dto.UserRoleDTO;
import com.bruce.entity.*;
import com.bruce.handlar.MyCustomException;
import com.bruce.pojo.Result;
import com.bruce.service.*;
import com.bruce.utils.AuthUtils;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController extends ApiController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private  RBloomFilter<Long> userBloomFilter;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SaltService saltService;

    @Autowired
    private UserService userService;

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody User user) {
        try {
            // 获取当前登录用户ID
            Long userId = StpUtil.getLoginIdAsLong();

            // 确保只能更新自己的信息
            if (user.getId() == null || !user.getId().equals(userId)) {
                return Result.error("无权修改其他用户信息");
            }

            // 更新用户信息
            boolean success = userService.updateById(user);
            if (success) {
                // 清除 Redis 缓存，确保下次查询时获取最新数据
                String cacheKey = "user_info:" + userId;
                try {
                    stringRedisTemplate.delete(cacheKey);
                    log.info("用户信息缓存已清除，用户ID: {}", userId);
                } catch (Exception e) {
                    log.error("清除用户信息缓存失败，用户ID: {}", userId, e);
                    // Redis 操作失败不应影响主流程
                }
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    @Autowired
    private FriendService friendService;

    @Resource
    private VideoService videoService; // 用于将对象转换为JSON字符串

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private DcSystemService dcSystemService;

    @Autowired
    private IdService idService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ExecutorService executorService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ObjectMapper objectMapper; // 用于将对象转换为JSON字符串

    public UserController() {
        // 创建一个固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/assignRoles")
    public R assignRoles(@RequestBody UserRoleDTO dto) {
        userRoleService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return R.ok("角色分配成功");
    }

    @GetMapping("/roles/{userId}")
    public R<List<Role>> getRoles(@PathVariable Long userId) {
        return R.ok(roleService.getRolesByUserId(userId));
    }

    @GetMapping("/permissions/{userId}")
    public R<List<Permission>> getPermissions(@PathVariable Long userId) {
        return R.ok(permissionService.getPermissionsByUserId(userId));
    }

    // 定义参数接收实体类
    static class LogoutRequest {
        private String action; // 操作类型：logout、logoutByDevice等
        private Object param; // 操作参数，根据action不同格式不同

        // getter和setter（必须，用于JSON反序列化）
        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Object getParam() {
            return param;
        }

        public void setParam(Object param) {
            this.param = param;
        }
    }

    @PostMapping("logout")
    public void handleLogoutAction(@RequestBody LogoutRequest request) {
        String action = request.getAction();
        Object param = request.getParam();

        if ("logout".equals(action)) {
            // 强制指定账号注销下线，参数应为用户ID（例如：10001）
            if (param instanceof Long) {
                StpUtil.logout((Long) param);
            } else {
                System.out.println("logout操作参数错误，应为Long类型用户ID");
            }
        } else if ("logoutByDevice".equals(action)) {
            // 强制指定账号在指定端注销下线，参数为数组：[用户ID, 端]
            if (param instanceof Object[] && ((Object[]) param).length == 2) {
                Object[] params = (Object[]) param;
                if (params[0] instanceof Long && params[1] instanceof String) {
                    StpUtil.logout((Long) params[0], (String) params[1]);
                } else {
                    System.out.println("logoutByDevice参数类型错误，应为[Long, String]");
                }
            } else {
                System.out.println("logoutByDevice参数错误，应为长度为2的数组");
            }
        } else if ("logoutByToken".equals(action)) {
            // 强制指定Token注销下线，参数为Token字符串
            if (param instanceof String) {
                StpUtil.logoutByTokenValue((String) param);
            } else {
                System.out.println("logoutByToken参数错误，应为String类型Token");
            }
        } else if ("kickout".equals(action)) {
            // 将指定账号踢下线，参数应为用户ID
            if (param instanceof Long) {
                StpUtil.kickout((Long) param);
            } else {
                System.out.println("kickout操作参数错误，应为Long类型用户ID");
            }
        } else if ("kickoutByDevice".equals(action)) {
            // 将指定账号在指定端踢下线，参数为数组：[用户ID, 端]
            if (param instanceof Object[] && ((Object[]) param).length == 2) {
                Object[] params = (Object[]) param;
                if (params[0] instanceof Long && params[1] instanceof String) {
                    StpUtil.kickout((Long) params[0], (String) params[1]);
                } else {
                    System.out.println("kickoutByDevice参数类型错误，应为[Long, String]");
                }
            } else {
                System.out.println("kickoutByDevice参数错误，应为长度为2的数组");
            }
        } else if ("kickoutByToken".equals(action)) {
            // 将指定Token踢下线，参数为Token字符串
            if (param instanceof String) {
                StpUtil.kickoutByTokenValue((String) param);
            } else {
                System.out.println("kickoutByToken参数错误，应为String类型Token");
            }
        } else {
            System.out.println("无效的操作: " + action);
        }
    }

    // 生成一个随机盐值
    public static String generateSalt(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder salt = new StringBuilder(length);
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            salt.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return salt.toString();
    }

    // 双重哈希加盐加密
    public static String saltedDoubleHashPassword(String password, String salt) {
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
            throw new MyCustomException(400, Constant.Error.HASH_TRANSFORM_ERROR);
        }
    }

    @PostMapping("/doLogin")
    @Transactional
    public Result doLogin(@RequestParam Map<String, String> params) {
        String userName = params.get(Constant.User.USER_NAME);
        String password = params.get(Constant.User.PASSWORD);
        String isCheckStr = params.get(Constant.User.ISCHECK);
        String verificationCode = params.get(Constant.User.VERIFICATION_CODE);
        String emailVerificationCode = params.get(Constant.User.EMAIL_VERIFICATION_CODE);
        String loginType = params.get(Constant.User.LOGIN_TYPE);
        boolean isCheck = "true".equalsIgnoreCase(isCheckStr);
        User user;
        try {
            if ("email".equals(loginType)) {
                // ===== 邮箱验证码登录 =====
                if (emailVerificationCode == null || emailVerificationCode.isEmpty()) {
                    return Result.error("邮箱验证码不能为空");
                }
                String code = stringRedisTemplate.opsForValue().get(Constant.User.EMAIL_CODE_PREFIX + userName);
                if (code == null || !emailVerificationCode.equals(code)) {
                    return Result.error(Constant.Error.CAPTCHA_ERROR);
                }
                // 热点数据：先从 Redis 查用户
                String userKey = Constant.User.USER_NAME + ":" + userName;
                String userJson = stringRedisTemplate.opsForValue().get(userKey);
                if (userJson != null) {
                    user = objectMapper.readValue(userJson, User.class);
                } else {
                    user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
                    if (user != null) {
                        // 存入 Redis，10 分钟过期
                        stringRedisTemplate.opsForValue().set(userKey, objectMapper.writeValueAsString(user), 10,
                                TimeUnit.MINUTES);
                    }
                }
                if (user == null) {
                    // 用户不存在则自动注册，随机8位密码可用于后续找回
                    String randomPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                    user = registerNewUser(userName, randomPassword);
                    // 注册后同步到 Redis
                    stringRedisTemplate.opsForValue().set(userKey, objectMapper.writeValueAsString(user), 10,
                            TimeUnit.MINUTES);
                }
                // 登录
                StpUtil.login(user.getId());

            } else {
                // ===== 用户名密码登录 =====
                if (password == null || password.isEmpty()) {
                    return Result.error("密码不能为空");
                }
                // 热点数据：先从 Redis 查用户
                String userKey = Constant.User.USER_NAME + ":" + userName;
                String userJson = stringRedisTemplate.opsForValue().get(userKey);
                if (userJson != null) {
                    user = objectMapper.readValue(userJson, User.class);
                } else {
                    user = userService.getUserByUsername(userName);
                    if (user != null) {
                        // 存入 Redis，10 分钟过期
                        stringRedisTemplate.opsForValue().set(userKey, objectMapper.writeValueAsString(user), 10,
                                TimeUnit.MINUTES);
                    }
                }
                if (user == null) {
                    return Result.error(Constant.Error.INVALID_CREDENTIALS);
                }
                // 使用优化后的用户服务验证密码
                if (!userService.verifyPassword(userName, password)) {
                    return Result.error(Constant.Error.INVALID_CREDENTIALS);
                }
                // 登录
                StpUtil.login(user.getId());
            }
            // 更新用户最后登录时间
            userService.updateLastLoginTime(user.getId());

            // 清除旧的权限缓存，确保获取最新权限
            AuthUtils.clearPermissionCache(user.getId());

            // 使用AuthUtils获取权限和角色列表
            List<String> roleList = AuthUtils.getRoleList(user.getId());
            List<String> permissionList = AuthUtils.getPermissionList(user.getId());

            // ===== 获取 token 和用户信息 =====
            String token = StpUtil.getTokenValue();
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            User userInfo = null;
            String userInfoKey = userId + Constant.User._USER_INFO;
            // 先从 Redis 中查询用户信息
            String userInfoJson = stringRedisTemplate.opsForValue().get(userInfoKey);
            if (userInfoJson != null) {
                // Redis 中存在，直接反序列化为对象
                userInfo = objectMapper.readValue(userInfoJson, User.class);
            } else {
                // Redis 中不存在，从数据库查询
                userInfo = userService.getById(userId);
                if (userInfo != null) {
                    // 将查询到的用户信息存入 Redis (10分钟)
                    userInfoJson = objectMapper.writeValueAsString(userInfo);
                    stringRedisTemplate.opsForValue().set(userInfoKey, userInfoJson, 10, TimeUnit.MINUTES);
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put(Constant.User.TOKEN, token);
            response.put(Constant.User.USER_INFO, userInfo);
            response.put(Constant.User.ROLE_LIST, roleList);
            response.put(Constant.User.PERMISSION_LIST, permissionList);
            return Result.success(response);
        } catch (MyCustomException e) {
            return Result.error("登录失败，请稍后重试");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败，请稍后重试");
        }
    }








    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    //todo  缓存穿透实例接口
    @GetMapping("/userInfo")
    public Result<UserInfo> userInfo(@RequestParam Map<String, Object> params) {
        String pageStr = String.valueOf(params.getOrDefault("page", ""));
        String pageSizeStr = String.valueOf(params.getOrDefault("pageSize", ""));
        String userId = String.valueOf(params.getOrDefault("userId", ""));
        String type = String.valueOf(params.getOrDefault("type", ""));

        if (userId.isEmpty()) {
            userId = String.valueOf(StpUtil.getLoginId());
        }

        Long userIdLong;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return Result.error("用户ID格式错误");
        }

        // 根据type选择缓存穿透解决方案
        User user = null;
        String key = "user_info:" + userId;
        String emptyKey = "empty_user:" + userId;

        // 方案1: 布隆过滤器 (type=bloom)
        if ("bloom".equals(type)) {
            if (!userBloomFilter.contains(userIdLong)) {
                    return Result.error("用户不存在");
            }
        }

        try {
            String userJson = stringRedisTemplate.opsForValue().get(key);
            if (userJson != null) {
                user = objectMapper.readValue(userJson, User.class);
            }

            // 方案1: 空值缓存 (type=empty)
            if ("empty".equals(type) && user == null) {
                String emptyValue = stringRedisTemplate.opsForValue().get(emptyKey);
                if ("null".equals(emptyValue)) {
                    return Result.error("用户不存在");
                }
            }
        } catch (Exception e) {
            log.error("Redis 用户信息反序列化失败，降级查询数据库", e);
        }

        // 方案3: 互斥锁 (type=lock)
        if (user == null) {
            if ("lock".equals(type)) {
                RLock lock = redissonClient.getLock("user_info_lock:" + userId);
                try {
                    // 尝试获取锁，最多等待5秒，锁自动释放时间30秒
                    if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                        try {
                            // 双重检查
                            String userJson = stringRedisTemplate.opsForValue().get(key);
                            if (userJson != null) {
                                user = objectMapper.readValue(userJson, User.class);
                            } else {
                                user = queryUserFromDb(userIdLong, key, emptyKey, type);
                            }
                        } catch (JsonMappingException e) {
                            throw new RuntimeException(e);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        // 获取锁失败，重试读取缓存
                        try {
                            String userJson = stringRedisTemplate.opsForValue().get(key);
                            if (userJson != null) {
                                user = objectMapper.readValue(userJson, User.class);
                            }
                        } catch (Exception e) {
                            log.error("重试读取缓存失败", e);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("获取锁异常", e);
                    Thread.currentThread().interrupt();
                }
            } else {
                user = queryUserFromDb(userIdLong, key, emptyKey, type);
            }
        }

        if (user == null) {
            return Result.error("用户不存在");
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);

        // 设置粉丝、关注和互关数量
        Integer fansCount = userRelationService.getFansCount(userIdLong);
        Integer followCount = userRelationService.getFollowCount(userIdLong);
        Integer mutualFollowCount = userRelationService.getMutualFollowCount(userIdLong);

        userInfo.setFansCount(fansCount);
        userInfo.setFollowCount(followCount);
        userInfo.setMutualFollowCount(mutualFollowCount);
        userInfo.setFollowers(fansCount);
        userInfo.setFollowing(followCount);
        userInfo.setMutualFollow(mutualFollowCount);

        try {
            if (pageStr.isEmpty() || pageSizeStr.isEmpty()) {
                List<Video> videoList = videoService.list(
                        new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId));
                userInfo.setVideoList(videoList);
            } else {
                long page, pageSize;
                try {
                    page = Long.parseLong(pageStr);
                    pageSize = Long.parseLong(pageSizeStr);
                } catch (NumberFormatException e) {
                    return Result.error("分页参数格式错误");
                }
                Page<Video> pageQry = new Page<>(page, pageSize);
                IPage<Video> videoPage = videoService.page(
                        pageQry,
                        new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId));
                userInfo.setVideoPage(videoPage);
            }
        } catch (Exception e) {
            log.error("查询视频信息失败", e);
            return Result.error("查询视频信息失败");
        }

        return Result.success(userInfo);
    }

    /**
     * 从数据库查询用户信息并处理缓存
     */
    private User queryUserFromDb(Long userId, String key, String emptyKey, String type) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));

        try {
            if (user != null) {
                String userJson = objectMapper.writeValueAsString(user);
                stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
                // 如果是空值缓存策略，删除空值标记
                if ("empty".equals(type)) {
                    stringRedisTemplate.delete(emptyKey);
                }
            } else {
                // 方案1: 缓存空值
                if ("empty".equals(type)) {
                    stringRedisTemplate.opsForValue().set(emptyKey, "null", 5, TimeUnit.MINUTES);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("用户信息序列化失败", e);
        }

        return user;
    }
    // 图片验证码接口
    @GetMapping("/getVerificationCode")
    public void getVerificationCode(HttpServletResponse response, @RequestParam Map<String, Object> params)
            throws IOException {
        String uuid = params.containsKey("uuid") ? String.valueOf(params.get("uuid")) : "";
        String userName = params.containsKey("userName") ? String.valueOf(params.get("userName")) : "";
        if (uuid == null || uuid.isEmpty()) {
            uuid = String.valueOf(System.currentTimeMillis()); // 生成唯一标识符
        }
        // 创建验证码对象（设置图片宽高和验证码字符数）
        SpecCaptcha captcha = new SpecCaptcha(120, 40, 5); // 验证码宽度 120px, 高度 40px，5个字符
        captcha.setCharType(Captcha.TYPE_DEFAULT); // 设置验证码类型为数字字母组合
        String intervalKey = "code:interval:" + userName;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(intervalKey))) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }
        stringRedisTemplate.opsForValue().set(intervalKey, "1", 60, TimeUnit.SECONDS);

        // 存入 Redis，过期时间 3 分钟`
        stringRedisTemplate.opsForValue().set("CAPTCHA_" + uuid + userName, captcha.text(), 3, TimeUnit.MINUTES);
        // 设置响应头
        response.setContentType("image/png"); // 设置返回类型为 PNG 格式的图片
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // 防止缓存
        response.setHeader("uuid", uuid); // 返回 uuid，供前端请求时使用
        // 使用 out() 方法生成并输出验证码图片
        try (ServletOutputStream out = response.getOutputStream()) {
            captcha.out(out); // 直接输出验证码到响应流
            out.flush();
        }
    }

    // 邮箱验证码接口
    @PostMapping("/getEmailVerificationCode")
    public void getEmailVerificationCodeService(@RequestParam String email) throws IOException {
        // 1. 生成验证码
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        // 2. 存入 Redis，有效期 5 分钟
        stringRedisTemplate.opsForValue().set("email:code:" + email, code, 5, TimeUnit.MINUTES);
        // 3. 构造消息
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("code", code);
        // 4. 发送到队列（work 模型只需一个队列）
        rabbitTemplate.convertAndSend("email.queue", message);
    }

    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    @PostMapping("/register")
    @Transactional
    public Result register(@RequestParam Map<String, Object> params) {
        // 1. 参数提取与校验
        String userName = String.valueOf(params.getOrDefault("userName", ""));
        String password = String.valueOf(params.getOrDefault("password", ""));
        boolean isCheck = Boolean.parseBoolean(String.valueOf(params.getOrDefault("isCheck", "false")));
        String verificationCode = String.valueOf(params.getOrDefault("verificationCode", ""));
        // 基础参数校验
        if (userName.isEmpty() || password.isEmpty()) {
            return Result.error("用户名和密码不能为空");
        }
        // 2. 验证码校验（如果需要）
        if (isCheck) {
            if (verificationCode.isEmpty()) {
                return Result.error("验证码不能为空");
            }
            String storedCode = stringRedisTemplate.opsForValue()
                    .get(Constant.User.EMAIL_CODE_PREFIX + userName);
            if (storedCode == null || !verificationCode.equalsIgnoreCase(storedCode)) {
                return Result.error("验证码错误");
            }
        }
        // 3. 用户注册处理
        try {
            User user = registerNewUser(userName, password);
            return Result.success(user); // 直接返回对象，让Result类处理序列化
        } catch (MyCustomException e) {
            log.error("业务异常: {}", e.getMessage());
            return Result.error(e.getMessage()); // 返回具体错误信息
        } catch (Exception e) {
            log.error("系统异常: {}", e.getMessage(), e);
            return Result.error("注册失败，请稍后重试");
        }
    }

    @Transactional
    public User registerNewUser(String username, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 查询是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User existingUser = userService.getOne(queryWrapper);

        if (existingUser != null) {
            throw new MyCustomException(400, Constant.Error.USER_REPEAT);
        }

        long userId = idService.SnowflakeGen();

        // 生成盐值并保存
        String saltStr = generateSalt(16);
        Salt salt = new Salt();
        salt.setUserId(userId);
        salt.setSalt(saltStr);
        saltService.save(salt);

        // 加密密码
        String encryptedPassword = saltedDoubleHashPassword(password, saltStr);

        // 设置用户基本信息
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setNickname(Constant.Chinese.NEW_USER + idService.SnowflakeGen());
        user.setAvatar(
                dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>()
                        .eq(DcSystem::getSysName, Constant.User.DEFAULT_USER_AVATAR))
                        .getSysValue());
        user.setBackImg(
                dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>()
                        .eq(DcSystem::getSysName, Constant.User.DEFAULT_USER_BACK_IMG))
                        .getSysValue());
        user.setRoleId(Constant.Role.USER);

        userService.save(user);

        return user;
    }

    // ===== 一、用户基础信息扩展接口 =====

    // 更新用户信息请求参数类
    static class UpdateInfoRequest {
        private Long userId;
        private String nickname;
        private String avatar;
        private String phone;
        private String gender; // 修改为String类型以匹配User实体
        private String birthday;

        // getter和setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }

    // 更新用户信息
    @PutMapping("/updateInfo")
    @Transactional
    public Result updateInfo(@RequestBody UpdateInfoRequest request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证手机号是否已被占用
            if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
                // 这里简化处理，实际应查询数据库验证
                System.out.println("验证手机号是否已被占用");
                // 模拟手机号已被占用的情况
                // return Result.error("手机号已被占用");
            }

            // 更新用户信息
            if (request.getNickname() != null)
                user.setNickname(request.getNickname());
            if (request.getAvatar() != null)
                user.setAvatar(request.getAvatar());
            if (request.getPhone() != null)
                user.setPhone(request.getPhone());
            if (request.getGender() != null)
                user.setGender(request.getGender());

            userService.updateById(user);

            // 清除Redis缓存
            String userInfoKey = userId + Constant.User._USER_INFO;
            stringRedisTemplate.delete(userInfoKey);

            return Result.success(null);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    // 修改密码请求参数类
    static class ChangePasswordRequest {
        private Long userId;
        private String oldPassword;
        private String newPassword;
        private String verificationCode;

        // getter和setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }

    // 修改密码
    @PostMapping("/changePassword")
    @Transactional
    public Result changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证原密码或验证码
            if (request.getVerificationCode() != null) {
                // 验证码方式（未登录状态）
                // 这里简化处理，实际应验证验证码
                System.out.println("验证验证码: " + request.getVerificationCode());
            } else {
                // 原密码方式（登录状态）
                if (request.getOldPassword() == null) {
                    return Result.error("原密码不能为空");
                }

                // 获取用户盐值并验证密码
                Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, userId));
                if (salt == null) {
                    return Result.error("用户信息异常");
                }

                String encryptedOldPassword = saltedDoubleHashPassword(request.getOldPassword(), salt.getSalt());
                if (!encryptedOldPassword.equals(user.getPassword())) {
                    return Result.error("原密码错误");
                }
            }

            // 更新密码
            String newSalt = generateSalt(16);
            String encryptedNewPassword = saltedDoubleHashPassword(request.getNewPassword(), newSalt);

            user.setPassword(encryptedNewPassword);
            userService.updateById(user);

            // 更新盐值
            Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, userId));
            salt.setSalt(newSalt);
            saltService.updateById(salt);

            return Result.success(null);
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.error("修改失败，请稍后重试");
        }
    }

    // ===== 二、用户状态与安全控制接口 =====

    // 用户状态响应类
    static class UserStatusResponse {
        private Integer status; // 1-正常，2-冻结，3-禁用
        private String reason; // 状态原因

        public UserStatusResponse(Integer status, String reason) {
            this.status = status;
            this.reason = reason;
        }

        // getter
        public Integer getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }
    }

    // 查询用户状态
    @GetMapping("/status/{userId}")
    public Result<UserStatusResponse> getUserStatus(@PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 这里简化处理，实际应根据用户状态字段返回
            // 模拟返回正常状态
            UserStatusResponse response = new UserStatusResponse(1, "");
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询用户状态失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // 冻结/解冻用户请求参数类
    static class FreezeRequest {
        private Long userId;
        private Boolean freeze;
        private String reason;

        // getter和setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Boolean getFreeze() {
            return freeze;
        }

        public void setFreeze(Boolean freeze) {
            this.freeze = freeze;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    // 冻结/解冻用户
    @PostMapping("/freeze")
    @Transactional
    public Result freezeUser(@RequestBody FreezeRequest request) {
        try {
            // 验证管理员权限（简化处理）
            boolean isAdmin = AuthUtils.getRoleList(StpUtil.getLoginIdAsLong()).contains("admin");
            if (!isAdmin) {
                return Result.error("无权限操作");
            }

            User user = userService.getById(request.getUserId());
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 更新用户状态（简化处理）
            System.out.println((request.getFreeze() ? "冻结" : "解冻") + "用户: " + request.getUserId() + ", 原因: "
                    + request.getReason());

            // 如果冻结，强制用户下线
            if (request.getFreeze()) {
                StpUtil.kickout(request.getUserId());
            }

            return Result.success(null);
        } catch (Exception e) {
            log.error("冻结/解冻用户失败", e);
            return Result.error("操作失败，请稍后重试");
        }
    }

    // 登录日志响应类
    static class LoginLog {
        private Long id;
        private String loginTime;
        private String device;
        private String ip;
        private String status;

        public LoginLog(Long id, String loginTime, String device, String ip, String status) {
            this.id = id;
            this.loginTime = loginTime;
            this.device = device;
            this.ip = ip;
            this.status = status;
        }

        // getter
        public Long getId() {
            return id;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public String getDevice() {
            return device;
        }

        public String getIp() {
            return ip;
        }

        public String getStatus() {
            return status;
        }
    }

    // 查询登录日志
    @GetMapping("/loginLogs")
    public Result<Map<String, Object>> getLoginLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟分页数据
            List<LoginLog> logs = new ArrayList<>();
            logs.add(new LoginLog(1L, "2025-11-18 08:00:00", "iPhone", "123.xxx.xxx.xxx", "success"));

            Map<String, Object> data = new HashMap<>();
            data.put("records", logs);
            data.put("total", 5);
            data.put("size", pageSize);
            data.put("current", page);

            return Result.success(data);
        } catch (Exception e) {
            log.error("查询登录日志失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // ===== 三、社交与关系接口 =====

    // 关注请求参数类
    static class FollowRequest {
        private Long targetUserId;
        private Boolean follow;

        // getter和setter
        public Long getTargetUserId() {
            return targetUserId;
        }

        public void setTargetUserId(Long targetUserId) {
            this.targetUserId = targetUserId;
        }

        public Boolean getFollow() {
            return follow;
        }

        public void setFollow(Boolean follow) {
            this.follow = follow;
        }
    }

    // 用户关注/取消关注
    @PostMapping("/follow")
    @Transactional
    public Result followUser(@RequestBody FollowRequest request) {
        try {
            Long currentUserId = Long.parseLong(StpUtil.getLoginId().toString());

            // 不能关注自己
            if (currentUserId.equals(request.getTargetUserId())) {
                return Result.error("不能关注自己");
            }

            // 验证目标用户是否存在
            if (userService.getById(request.getTargetUserId()) == null) {
                return Result.error("目标用户不存在");
            }

            // 模拟关注/取消关注操作
            System.out.println((request.getFollow() ? "关注" : "取消关注") + "用户: " + request.getTargetUserId());

            return Result.success(null);
        } catch (Exception e) {
            log.error("关注操作失败", e);
            return Result.error("操作失败，请稍后重试");
        }
    }

    // 关注/粉丝用户信息类
    static class UserBriefInfo {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;

        public UserBriefInfo(Long id, String username, String nickname, String avatar) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
            this.avatar = avatar;
        }

        // getter
        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    // 查询关注列表
    @GetMapping("/follows")
    public Result<Map<String, Object>> getFollows(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟分页数据
            List<UserBriefInfo> follows = new ArrayList<>();
            follows.add(new UserBriefInfo(456L, "xxx", "昵称", "xxx.jpg"));

            Map<String, Object> data = new HashMap<>();
            data.put("records", follows);
            data.put("total", 3);
            data.put("size", pageSize);
            data.put("current", page);

            return Result.success(data);
        } catch (Exception e) {
            log.error("查询关注列表失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // 查询粉丝列表
    @GetMapping("/fans")
    public Result<Map<String, Object>> getFans(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟分页数据
            List<UserBriefInfo> fans = new ArrayList<>();
            fans.add(new UserBriefInfo(789L, "fan1", "粉丝1", "fan1.jpg"));

            Map<String, Object> data = new HashMap<>();
            data.put("records", fans);
            data.put("total", 5);
            data.put("size", pageSize);
            data.put("current", page);

            return Result.success(data);
        } catch (Exception e) {
            log.error("查询粉丝列表失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // ===== 四、账号关联与第三方登录接口 =====

    // 第三方账号请求参数类
    static class ThirdAccountRequest {
        private String thirdType; // wechat/qq/facebook等
        private String thirdId;
        private String accessToken;

        // getter和setter
        public String getThirdType() {
            return thirdType;
        }

        public void setThirdType(String thirdType) {
            this.thirdType = thirdType;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    // 绑定第三方账号
    @PostMapping("/bindThirdAccount")
    @Transactional
    public Result bindThirdAccount(@RequestBody ThirdAccountRequest request) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());

            // 验证第三方账号是否已绑定其他用户
            System.out.println("验证第三方账号是否已绑定: " + request.getThirdType() + "-" + request.getThirdId());
            // 模拟已绑定的情况
            // return Result.error("该" + request.getThirdType() + "已绑定其他账号");

            // 模拟绑定操作
            System.out.println("绑定第三方账号: " + request.getThirdType() + " 到用户: " + userId);

            return Result.success(null);
        } catch (Exception e) {
            log.error("绑定第三方账号失败", e);
            return Result.error("绑定失败，请稍后重试");
        }
    }

    // 第三方登录
    @PostMapping("/thirdLogin")
    @Transactional
    public Result thirdLogin(@RequestBody ThirdAccountRequest request) {
        try {
            // 验证第三方账号信息
            System.out.println("验证第三方账号: " + request.getThirdType() + "-" + request.getThirdId());

            // 模拟查找用户
            User user = null;
            // 实际应查询数据库中绑定了该第三方账号的用户

            // 如果用户不存在，可选择自动注册
            if (user == null) {
                String randomPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                user = registerNewUser("third_" + request.getThirdId(), randomPassword);
                // 同时绑定第三方账号
                System.out.println("自动注册并绑定第三方账号");
            }

            // 登录
            StpUtil.login(user.getId());

            // 更新用户最后登录时间
            userService.updateLastLoginTime(user.getId());

            // 清除旧的权限缓存
            AuthUtils.clearPermissionCache(user.getId());

            // 获取权限和角色列表
            List<String> roleList = AuthUtils.getRoleList(user.getId());
            List<String> permissionList = AuthUtils.getPermissionList(user.getId());

            // 返回token和用户信息
            String token = StpUtil.getTokenValue();
            Map<String, Object> response = new HashMap<>();
            response.put(Constant.User.TOKEN, token);
            response.put(Constant.User.USER_INFO, user);
            response.put(Constant.User.ROLE_LIST, roleList);
            response.put(Constant.User.PERMISSION_LIST, permissionList);

            return Result.success(response);
        } catch (Exception e) {
            log.error("第三方登录失败", e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    // ===== 五、其他实用接口 =====

    // 用户标签请求参数类
    static class UserTagsRequest {
        private Long userId;
        private List<String> tags;

        // getter和setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    // 查询用户标签
    @GetMapping("/tags")
    public Result<List<String>> getUserTags(@RequestParam(required = false) Long userId) {
        try {
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟返回标签列表
            List<String> tags = Arrays.asList("学生", "活跃用户");
            return Result.success(tags);
        } catch (Exception e) {
            log.error("查询用户标签失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // 添加用户标签
    @PostMapping("/tags")
    @Transactional
    public Result addUserTags(@RequestBody UserTagsRequest request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟添加标签操作
            System.out.println("为用户添加标签: " + userId + " - " + String.join(", ", request.getTags()));

            return Result.success(null);
        } catch (Exception e) {
            log.error("添加用户标签失败", e);
            return Result.error("添加失败，请稍后重试");
        }
    }

    // 删除用户标签
    @DeleteMapping("/tags")
    @Transactional
    public Result deleteUserTags(@RequestBody UserTagsRequest request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证用户是否存在
            if (userService.getById(userId) == null) {
                return Result.error("用户不存在");
            }

            // 模拟删除标签操作
            System.out.println("为用户删除标签: " + userId + " - " + String.join(", ", request.getTags()));

            return Result.success(null);
        } catch (Exception e) {
            log.error("删除用户标签失败", e);
            return Result.error("删除失败，请稍后重试");
        }
    }

    // 注销账号请求参数类
    static class CancelAccountRequest {
        private Long userId;
        private String password;
        private Boolean confirm;

        // getter和setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Boolean getConfirm() {
            return confirm;
        }

        public void setConfirm(Boolean confirm) {
            this.confirm = confirm;
        }
    }

    // 注销账号
    @PostMapping("/cancelAccount")
    @Transactional
    public Result cancelAccount(@RequestBody CancelAccountRequest request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证是否确认注销
            if (request.getConfirm() == null || !request.getConfirm()) {
                return Result.error("请确认注销账号");
            }

            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证密码
            Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, userId));
            if (salt == null) {
                return Result.error("用户信息异常");
            }

            String encryptedPassword = saltedDoubleHashPassword(request.getPassword(), salt.getSalt());
            if (!encryptedPassword.equals(user.getPassword())) {
                return Result.error("密码错误");
            }

            // 模拟提交注销申请
            System.out.println("用户提交注销申请: " + userId);

            // 强制用户下线
            StpUtil.kickout(userId);

            return Result.success(null);
        } catch (Exception e) {
            log.error("注销账号失败", e);
            return Result.error("操作失败，请稍后重试");
        }
    }

}
