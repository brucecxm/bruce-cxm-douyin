package com.bruce.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dict.Constant;
import com.bruce.entity.*;
import com.bruce.feign.systemClient;
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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    private systemClient systemClients;

    @Autowired
    private DcSystemService dcSystemService;

    @Autowired
    private IdService idService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private ExecutorService executorService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ObjectMapper objectMapper;  // 用于将对象转换为JSON字符串

    public UserController() {
        // 创建一个固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(10);
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
    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    public Result doLogin(@RequestParam Map<String, String> params) {
        String userName = params.get(Constant.User.USER_NAME);
        String password = params.get(Constant.User.PASSWORD);
        String emailVerificationCode = params.get(Constant.User.EMAIL_VERIFICATION_CODE);
        // 查询符合条件的单条记录
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,userName));
        if (user == null)
            return Result.error(Constant.Error.USER_INVALID);
        if (!emailVerificationCode.isEmpty() && emailVerificationCode != null && !emailVerificationCode.equals("")) {
            String code = stringRedisTemplate.opsForValue().get(Constant.User.EMAIL_CODE_PREFIX + userName);
            if (!emailVerificationCode.equals(code))
                return Result.error(Constant.Error.LOGIN_FAILED);
        } else {
            //todo 注销用户的时候 一定要记得注销掉用户的盐值
            // 获取盐值
            Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId,user.getId()));
            // 加密输入的密码
            String encryptedPassword = saltedDoubleHashPassword(password, salt.getSalt());
// 判断用户名和加密后的密码是否匹配，验证通过进入登录流程
            if (!(user.getUsername().equals(userName) && encryptedPassword.equals(user.getPassword()))) {
                return Result.error(Constant.Error.LOGIN_FAILED);
            }
        }
        // 1. 默认使用 sa-token 登录方式
            DcSystem result = dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>().eq(DcSystem::getSysName,Constant.User.LOGIN_TYPE));
            String login_type =Constant.User.SA_TOKEN; // 默认登录方式为 sa-token
            if (result != null) {
                // 获取配置项中的值（可能是 "sa-token" 或 "jwt"）
                 login_type =  result.getSysValue();
            }
        // 构建返回给前端的 token 响应数据
        Map response = new HashMap<>();
        // 如果配置的是 sa-token 方式
        if (login_type.equals(Constant.User.SA_TOKEN)) {
            // 使用 Sa-Token 登录，传入用户 ID，表示登录成功
            StpUtil.login(user.getId());
//                stringRedisTemplate.opsForValue().set();
            // 获取生成的 token 值
            String token_login = StpUtil.getTokenValue();
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getId, user.getId()); // 构建查询条件：username 等于传入的 username
            User user1 = userService.getOne(queryWrapper1); // 执行查询，返回满足条件的第一条 User 记录
            // 将 token 放入返回 map
            response.put(Constant.User.TOKEN, token_login);
            response.put(Constant.User.USER_INFO, user1);
            String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
            Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
            String useridStr = String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
            try {
                String userJson = objectMapper.writeValueAsString(user);
                String key = useridStr + Constant.User._USER_INFO;
                stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            // 如果配置的是 jwt 方式
        } else if (login_type.equals(Constant.User.JWT)) {
            // 使用自定义 jwt 工具类生成 token（一般以用户名作为 payload）
            String token = jwtUtil.generateToken(user.getUsername());
            // 将 token 放入返回 map
            response.put(Constant.User.TOKEN, token);
        } else {
            // 如果配置了不支持的 token 类型，可以在这里补充报错或记录日志（建议完善）
        }
        // 登录成功，返回包含 token 的成功结果给前端
        return Result.success(response);
    }

    @GetMapping("/auth")
    public Map<String, Object> getAuth(
            @RequestParam(required = false) Long userid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Object UserIdObj = StpUtil.getLoginId();
        Long loginUserId = UserIdObj != null ? Long.parseLong(UserIdObj.toString()) : 0L;
        if (userid == null) {
            userid = loginUserId;  // 使用当前登录用户ID
        }
        Map<String, Object> result = new HashMap<>();
        // 查询用户信息
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getId, userid);
        User userInfo = userService.getOne(userQuery);
        result.put("userInfo", userInfo);
        // 查询视频分页数据
        Page<Video> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getUserId, userid);
        IPage<Video> videoPage = videoService.page(pageInfo, videoQuery);
        result.put("videobox", videoPage.getRecords());
        result.put("total", videoPage.getTotal());
        return result;
    }

    // 图片验证码接口
    @PutMapping("/getVerificationCode")
    public void getVerificationCode(HttpServletResponse response, @RequestParam(required = false) String uuid) throws IOException {
        if (uuid == null) {
            uuid = String.valueOf(System.currentTimeMillis()); // 生成唯一标识符
        }
        // 创建验证码对象（设置图片宽高和验证码字符数）
        SpecCaptcha captcha = new SpecCaptcha(120, 40, 5); // 验证码宽度 120px, 高度 40px，5个字符
        captcha.setCharType(Captcha.TYPE_DEFAULT); // 设置验证码类型为数字字母组合
        // 存入 Redis，过期时间 3 分钟`
        stringRedisTemplate.opsForValue().set("CAPTCHA_" + uuid, captcha.text(), 3, TimeUnit.MINUTES);
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

    // 图片验证码接口
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


    @PutMapping("/getVerificationCodeImg")
    public void getVerificationCodeImg(HttpServletResponse response, @RequestParam(required = false) String uuid) throws IOException {
        if (uuid == null) {
            uuid = String.valueOf(System.currentTimeMillis()); // 生成唯一标识符
        }
        // 创建验证码对象（设置图片宽高和验证码字符数）
        SpecCaptcha captcha = new SpecCaptcha(120, 40, 5);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 存入 Redis，过期时间 3 分钟
        stringRedisTemplate.opsForValue().set("CAPTCHA_" + uuid, captcha.text(), 3, TimeUnit.MINUTES);
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("uuid", uuid);
        // 使用 out() 方法生成并输出验证码图片
        ServletOutputStream out = response.getOutputStream();
        captcha.out(out); // 直接输出验证码到响应流
        out.flush();
        out.close();
    }


    //*******************sa-token***********************







    @PostMapping("/register")
    @Transactional
    public Result register(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User u = userService.getOne(queryWrapper); // 获取用户名对应的用户
        User user = new User();
        long userid = idService.SnowflakeGen();
        Salt saltone = new Salt();
        // 生成盐值
        String salt = generateSalt(16);
        saltone.setUserId(userid);
        // 加密密码
        String encryptedPassword = saltedDoubleHashPassword(password, salt);
        saltone.setSalt(salt);
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setAvatar( dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>().eq(DcSystem::getSysName, Constant.User.DEFAULT_USER_AVATAR)).getSysValue());
        user.setBackImg(dcSystemService.getOne(new LambdaQueryWrapper<DcSystem>().eq(DcSystem::getSysName, Constant.User.DEFAULT_USER_BACK_IMG)).getSysValue());
        user.setRoleId(Constant.Role.USER);
        if (u != null)
            throw new MyCustomException(400, Constant.Error.USER_REPEAT);
            //注册
            user.setId(userid);
            user.setNickname(Constant.Chinese.NEW_USER + idService.SnowflakeGen());
            userService.save(user);
            LambdaQueryWrapper queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username); // 等价于 SQL 中的 WHERE username = ?
            User userResult = userService.getOne(queryWrapper);
            saltService.save(saltone);
            String userStr;
            ObjectMapper objectMapper = new ObjectMapper();
            userStr = objectMapper.writeValueAsString(userResult);
            return Result.success(userStr);
    }

    // 哈希密码
    public static String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash) + ":" + Base64.getEncoder().encodeToString(salt); // 返回盐值和哈希值
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public Result isLogin() {
        // 获取：当前账号所拥有的权限集合
        List<String> permission = StpUtil.getPermissionList();
        for (String permissiontemp : permission
        ) {
            if (permissiontemp.equals("user.get")
            ) {
// 判断：当前账号是否拥有指定角色, 返回 true 或 false
                if (StpUtil.hasRole("super-admin")) {
// 在登录时缓存 user 对象
                    Object user1 = StpUtil.getSession().get("user");
                    // 然后我们就可以在任意处使用这个 user 对象
// 获取当前 Token 的 Token-Session 对象
                    SaSession tokenSession = StpUtil.getTokenSession();
                    String data = stringRedisTemplate.opsForValue().get("LOGIN_TYPE:PASSWORD");
                    System.out.println("Sa-Token Data: " + data);
                    // 封禁指定账号
                    // 先踢下线
                    StpUtil.kickout(10001);
                    StpUtil.disable(10001, 86400);
                    return Result.success("当前会话是否登录：" + StpUtil.isLogin());
                }
            }
        }
        return Result.error("无权查看");
    }

    //生成四位的验证吗
    @GetMapping("/getYanzheng")
    public Result<String> getYanzheng() {
        // 生成四位随机验证码
        String verificationCode = generateRandomCode(4);
        // 将验证码存储到 Redis 中，设置过期时间为5分钟（可根据需求调整）
        stringRedisTemplate.opsForValue().set("yancode", verificationCode, 5, TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend("YanZhengQueue", verificationCode);
        // 返回验证码
        return Result.success(verificationCode);
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return code.toString();
    }

    @GetMapping("/userInfo")
    public MeInfo userInfo() {
        Object userId = StpUtil.getLoginId();
        System.out.println("当前用户的ID: " + userId);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        MeInfo meInfo = new MeInfo();
        User user = userService.getOne(queryWrapper);
        LambdaQueryWrapper<Video> queryWrapperV = new LambdaQueryWrapper<>();
        queryWrapperV.eq(Video::getUserId, userId);
        List<Video> videoList = videoService.list(queryWrapperV);
// 使用 BeanUtils 复制相同属性
        BeanUtils.copyProperties(user, meInfo);
        meInfo.setVideoList(videoList);
        String useridStr = String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
//        List<Video> videoList = videoService.getVideosByUserId(user.getId());
        try {
            String userJson = objectMapper.writeValueAsString(user);
            String key = useridStr + "_user_info";
            stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return meInfo;
    }

}
