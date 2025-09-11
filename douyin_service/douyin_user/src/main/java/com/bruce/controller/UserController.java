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
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private RedisUtil redisUtil;

    @Autowired
    private SaltService saltService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Resource
    private VideoService videoService;  // 用于将对象转换为JSON字符串

    @Autowired
    private DcSystemService dcSystemService;

    @Autowired
    private IdService idService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ExecutorService executorService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ObjectMapper objectMapper;  // 用于将对象转换为JSON字符串

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


    @RequestMapping("logout")
    public void handleLogoutAction(String action, Object param) {
        if ("logout".equals(action)) {
            // 强制指定账号注销下线，参数应为用户ID（例如：10001）
            if (param instanceof Integer) {
                StpUtil.logout((Integer) param);
            }
        } else if ("logoutByDevice".equals(action)) {
            // 强制指定账号在指定端注销下线，参数为 Object[] 数组：[用户ID, 端]
            if (param instanceof Object[] && ((Object[]) param).length == 2) {
                Object[] params = (Object[]) param;
                if (params[0] instanceof Integer && params[1] instanceof String) {
                    StpUtil.logout((Integer) params[0], (String) params[1]);
                }
            }
        } else if ("logoutByToken".equals(action)) {
            // 强制指定 Token 注销下线，参数为 Token 字符串
            if (param instanceof String) {
                StpUtil.logoutByTokenValue((String) param);
            }
        } else if ("kickout".equals(action)) {
            // 将指定账号踢下线，参数应为用户ID
            if (param instanceof Integer) {
                StpUtil.kickout((Integer) param);
            }
        } else if ("kickoutByDevice".equals(action)) {
            // 将指定账号在指定端踢下线，参数为 Object[] 数组：[用户ID, 端]
            if (param instanceof Object[] && ((Object[]) param).length == 2) {
                Object[] params = (Object[]) param;
                if (params[0] instanceof Integer && params[1] instanceof String) {
                    StpUtil.kickout((Integer) params[0], (String) params[1]);
                }
            }
        } else if ("kickoutByToken".equals(action)) {
            // 将指定 Token 踢下线，参数为 Token 字符串
            if (param instanceof String) {
                StpUtil.kickoutByTokenValue((String) param);
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
                user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
                if (user == null) {
                    // 用户不存在则自动注册，随机8位密码可用于后续找回
                    String randomPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                    user = registerNewUser(userName, randomPassword);
                }
                // 登录
                StpUtil.login(user.getId());

            } else {
                // ===== 用户名密码登录 =====
                if (password == null || password.isEmpty()) {
                    return Result.error("密码不能为空");
                }
                user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
                if (user == null) {
                    return Result.error(Constant.Error.USER_INVALID);
                }

                // 验证码可选校验
                if (isCheck) {
                    String code = stringRedisTemplate.opsForValue().get(Constant.User.EMAIL_CODE_PREFIX + userName);
                    if (verificationCode == null || verificationCode.isEmpty()) {
                        return Result.error("验证码不能为空");
                    }
                    if (code == null || !verificationCode.equalsIgnoreCase(code)) {
                        return Result.error("验证码错误");
                    }
                }
                Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, user.getId()));
                if (salt == null) {
                    return Result.error("未获取到用户盐值，请联系管理员");
                }
                String encryptedPassword = saltedDoubleHashPassword(password, salt.getSalt());
                if (!encryptedPassword.equals(user.getPassword())) {
                    return Result.error(Constant.Error.INVALID_CREDENTIALS);
                }
                // 登录
                StpUtil.login(user.getId());

            }
            //Sa-Token 的权限管理本质上是基于会话的，但权限数据的来源需要你自己实现
            // 因此登录之后 要自己从数据库获取其权限角色设置  后面登录状态下能快速获取
            //一般都是通过一个接口查询  sa-token暴露出了获取权限和角色的接口
            //请查看StpInterfaceImpl  这里是获取权限和角色的逻辑
            List<String> roleList = StpUtil.getRoleList();
            List<String> permissionList = StpUtil.getPermissionList();
            // ===== 获取 token 和用户信息 =====
            String token = StpUtil.getTokenValue();
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            User userInfo = null;
            String userKey = userId + Constant.User._USER_INFO;
            // 先从 Redis 中查询用户信息
            String userJson = stringRedisTemplate.opsForValue().get(userKey);
            if (userJson != null) {
                // Redis 中存在，直接反序列化为对象
                userInfo = objectMapper.readValue(userJson, User.class);
            } else {
                // Redis 中不存在，从数据库查询
                userInfo = userService.getById(userId);
                if (userInfo != null) {
                    // 将查询到的用户信息存入 Redis (10分钟)
                    userJson = objectMapper.writeValueAsString(userInfo);
                    stringRedisTemplate.opsForValue().set(userKey, userJson, 10, TimeUnit.MINUTES);
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put(Constant.User.TOKEN, token);
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


    @GetMapping("/userInfo")
    public Result<UserInfo> userInfo(@RequestParam Map<String, Object> params) {
        String pageStr = String.valueOf(params.getOrDefault("page", ""));
        String pageSizeStr = String.valueOf(params.getOrDefault("pageSize", ""));
        String userId = String.valueOf(params.getOrDefault("userId", ""));

        if (userId.isEmpty()) {
            userId = String.valueOf(StpUtil.getLoginId());
        }

        String key = "user_info:" + userId;
        User user = null;

        try {
            String userJson = stringRedisTemplate.opsForValue().get(key);
            if (userJson != null) {
                user = objectMapper.readValue(userJson, User.class);
            }
        } catch (Exception e) {
            // 可打印日志但不中断流程
            log.error("Redis 用户信息反序列化失败，降级查询数据库", e);
        }

        if (user == null) {
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
            if (user == null) {
                return Result.error("用户不存在");
            }
            try {
                String userJson = objectMapper.writeValueAsString(user);
                stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                log.error("用户信息序列化失败", e);
            }
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);

        try {
            if (pageStr.isEmpty() || pageSizeStr.isEmpty()) {
                List<Video> videoList = videoService.list(
                        new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId)
                );
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
                        new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId)
                );
                userInfo.setVideoPage(videoPage);
            }
        } catch (Exception e) {
            log.error("查询视频信息失败", e);
            return Result.error("查询视频信息失败");
        }

        return Result.success(userInfo);
    }


    // 图片验证码接口
    @GetMapping("/getVerificationCode")
    public void getVerificationCode(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        String uuid = params.containsKey("uuid") ?
                String.valueOf(params.get("uuid")) :
                "";
        String userName = params.containsKey("userName") ?
                String.valueOf(params.get("userName")) :
                "";
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


    //简单的参数类型 如果没有用参数注解  spring会默认把参数前面加上@RequestParam
//简单的参数类型 如果没有用参数注解  spring会默认把参数前面加上@RequestParam
//简单的参数类型 如果没有用参数注解  spring会默认把参数前面加上@RequestParam
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
    public User registerNewUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
                        .getSysValue()
        );
        user.setBackImg(
                dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>()
                                .eq(DcSystem::getSysName, Constant.User.DEFAULT_USER_BACK_IMG))
                        .getSysValue()
        );
        user.setRoleId(Constant.Role.USER);

        userService.save(user);

        return user;
    }


}
