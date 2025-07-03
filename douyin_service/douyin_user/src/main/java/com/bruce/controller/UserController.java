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
    private RabbitTemplate rabbitTemplate;

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
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    @PostMapping("/doLogin")
    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    public Result doLogin(@RequestParam Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String emailVerificationCode = params.get("emailVerificationCode");
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        System.out.println();
        // 查询符合条件的单条记录
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return Result.error("请核对用户名是否正确");
        }
        if (!emailVerificationCode.isEmpty() && emailVerificationCode != null && !emailVerificationCode.equals("")) {
            String code = stringRedisTemplate.opsForValue().get("email:code:" + username);
            if (!emailVerificationCode.equals(code)) {
                return Result.error("登录失败");
            }
        } else {
            //todo 注销用户的时候 一定要记得注销掉用户的盐值
            LambdaQueryWrapper<Salt> queryWrapperone = new LambdaQueryWrapper<>();
            // 使用查询条件，假设Salt类有一个用户名字段叫username
            queryWrapperone.eq(Salt::getUserId, user.getId());
            // 获取盐值
            Salt salt = saltService.getOne(queryWrapperone);
            // 加密输入的密码
            String encryptedPassword = saltedDoubleHashPassword(password, salt.getSalt());
// 判断用户名和加密后的密码是否匹配，验证通过进入登录流程
            if (!(user.getUsername().equals(username) && encryptedPassword.equals(user.getPassword()))) {
                return Result.error("登录失败");
            }
        }
        // 1. 默认使用 sa-token 登录方式
//            R result = systemClients.findById(1); // 从配置中心或系统管理服务查询id为1的登录方式配置
        String login_token = "sa-token"; // 默认登录方式为 sa-token
//            if (result != null) {
//                // 获取配置项中的值（可能是 "sa-token" 或 "jwt"）
//                HashMap resultMap = (HashMap) result.getData();
//                login_token = String.valueOf(resultMap.get("value"));
//            }
        // 构建返回给前端的 token 响应数据
        Map response = new HashMap<>();
        // 如果配置的是 sa-token 方式
        if (login_token.equals("sa-token")) {
            // 使用 Sa-Token 登录，传入用户 ID，表示登录成功
            StpUtil.login(user.getId());
//                stringRedisTemplate.opsForValue().set();
            // 获取生成的 token 值
            String token_login = StpUtil.getTokenValue();
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getId, user.getId()); // 构建查询条件：username 等于传入的 username
            User user1 = userService.getOne(queryWrapper1); // 执行查询，返回满足条件的第一条 User 记录
            // 将 token 放入返回 map
            response.put("token", token_login);
            response.put("userInfo", user1);
            String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
            Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
            String useridStr = String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
            try {
                String userJson = objectMapper.writeValueAsString(user);
                String key = useridStr + "_user_info";
                stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            // 如果配置的是 jwt 方式
        } else if (login_token.equals("jwt")) {
            // 使用自定义 jwt 工具类生成 token（一般以用户名作为 payload）
            String token = jwtUtil.generateToken(user.getUsername());
            // 将 token 放入返回 map
            response.put("token", token);
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
            queryWrapper.eq(User::getUsername, username); // 等价于 SQL 中的 WHERE username = ?
            User userResult = userService.getOne(queryWrapper);
            saltService.save(saltone);
            String userStr;
            ObjectMapper objectMapper = new ObjectMapper();
            userStr = objectMapper.writeValueAsString(userResult);
            return Result.success(userStr);
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
        BeanUtils.copyProperties(user, meInfo);
        meInfo.setVideoList(videoList);
        String useridStr = String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
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
