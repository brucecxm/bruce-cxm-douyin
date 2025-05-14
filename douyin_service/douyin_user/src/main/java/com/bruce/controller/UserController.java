package com.bruce.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.entity.MeInfo;
import com.bruce.entity.Salt;
import com.bruce.entity.User;
import com.bruce.entity.Video;
import com.bruce.feign.systemClient;
import com.bruce.pojo.Result;
import com.bruce.service.SaltService;
import com.bruce.service.UserService;
import com.bruce.service.VideoService;
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
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SaltService saltService;


    //*******************sa-token***********************
    @Autowired
    private UserService userService;


    @RequestMapping("cha")
    public List handleLogoutAction() {

        System.out.println("comming");

List one= userService.list();//查数据库


List two=new ArrayList();
String ss="sss";
int ass=1;
two.add(ss);
two.add(ass);
return two;

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


    @RequestMapping("getall")
    public R getAllUser() {

        List<User> one = userService.list();

        return R.ok(one);

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


    @Autowired
    private systemClient systemClients;


    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public Result doLogin(String username, String password) throws Exception {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        // 查询符合条件的单条记录
        User user = userService.getOne(queryWrapper);

        if (user == null) {
            return Result.error("请核对用户名是否正确");
        }
        LambdaQueryWrapper<Salt> queryWrapperone = new LambdaQueryWrapper<>();
        // 使用查询条件，假设Salt类有一个用户名字段叫username
        queryWrapperone.eq(Salt::getUsername, username);
        // 获取盐值
        Salt salt = saltService.getOne(queryWrapperone);
        // 加密输入的密码
        String encryptedPassword = saltedDoubleHashPassword(password, salt.getSalt());
// 判断用户名和加密后的密码是否匹配，验证通过进入登录流程
        if (user.getUsername().equals(username) && encryptedPassword.equals(user.getPassword())) {

            // 1. 默认使用 sa-token 登录方式
//            R result = systemClients.findById(1); // 从配置中心或系统管理服务查询id为1的登录方式配置
            String login_token = "sa-token"; // 默认登录方式为 sa-token

//            if (result != null) {
//                // 获取配置项中的值（可能是 "sa-token" 或 "jwt"）
//                HashMap resultMap = (HashMap) result.getData();
//                login_token = String.valueOf(resultMap.get("value"));
//            }

            // 构建返回给前端的 token 响应数据
            Map<String, String> response = new HashMap<>();

            // 如果配置的是 sa-token 方式
            if (login_token.equals("sa-token")) {
                // 使用 Sa-Token 登录，传入用户 ID，表示登录成功
                StpUtil.login(user.getId());
//                stringRedisTemplate.opsForValue().set();
                // 获取生成的 token 值
                String token_login = StpUtil.getTokenValue();
                // 将 token 放入返回 map
                response.put("token", token_login);
                String loginId = StpUtil.getLoginId().toString();  // 转换为字符串
                Long userId = Long.parseLong(loginId);  // 将字符串转为 Long 类型
                String useridStr= String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
                try {
                    String userJson = objectMapper.writeValueAsString(user);

                    String key=useridStr+"_user_info";
                    stringRedisTemplate.opsForValue().set(key,userJson,10,TimeUnit.MINUTES);
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

        return Result.error("登录失败");
    }

    // 图片验证码接口
    @PutMapping("/getVerificationCode")
    @ResponseBody  // 直接返回响应体
    public void getVerificationCode(HttpServletResponse response, @RequestParam(required = false) String uuid) throws IOException {
        if (uuid == null) {
            uuid = String.valueOf(System.currentTimeMillis()); // 生成唯一标识符
        }

        // 创建验证码对象（设置图片宽高和验证码字符数）
        SpecCaptcha captcha = new SpecCaptcha(120, 40, 5); // 验证码宽度 120px, 高度 40px，5个字符
        captcha.setCharType(Captcha.TYPE_DEFAULT); // 设置验证码类型为数字字母组合

        // 存入 Redis，过期时间 3 分钟
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


    //短信验证码接口
    @PutMapping("/getVerificationCodeImg")
    @ResponseBody  // Adding @ResponseBody to return a response body
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


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AmqpTemplate amqpTemplate;
    private ExecutorService executorService;

    public UserController() {
        // 创建一个固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(10);
    }


    @PostMapping("/register")
    public Result register(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {

        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User u = userService.getOne(queryWrapper); // 获取用户名对应的用户

        User user = new User();

        Salt saltone = new Salt();


        // 生成盐值
        String salt = generateSalt(16);

        // 加密密码
        String encryptedPassword = saltedDoubleHashPassword(password, salt);

        saltone.setSalt(salt);
        saltone.setUsername(username);


        user.setUsername(username);
        user.setPassword(encryptedPassword);
        if (u == null) {
            //没有占用
            //注册
            userService.save(user);
            LambdaQueryWrapper queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username); // 等价于 SQL 中的 WHERE username = ?
            User userResult=userService.getOne(queryWrapper);
            saltService.save(saltone);
            executorService.submit(() -> {
                try {
                    // 发送消息的代码
                    amqpTemplate.convertAndSend("create_walletone_exampleExchange", "create_walletone_RoutingKey", user.getId());
                } catch (Exception e) {
                    int maxRetryCount = 10;  // 最大重试次数
                    int retryCount = 0;
                    int retryInterval = 3600 * 1000;  // 每次重试的间隔，单位为毫秒，1 小时
                    retryCount++;
                    while (retryCount < maxRetryCount) {
                        try {
                            // 发送消息
                            amqpTemplate.convertAndSend("create_walletone_exampleExchange", "create_walletone_RoutingKey", user.getId());
                            return;  // 如果发送成功，则直接返回

                        } catch (Exception ee) {
                            retryCount++;
                            logger.error("发送消息失败, userId: " + user.getId() + ", 尝试次数: " + retryCount, e);

                            if (retryCount >= maxRetryCount) {
                                // 达到最大重试次数，记录失败并退出
                                logger.error("最大重试次数已达到, 发送失败, userId: " + user.getId());
                                // 可以触发补偿机制，记录日志等
                                break;
                            }

                            // 等待 1 小时后再试
                            try {
                                Thread.sleep(retryInterval);  // 等待 1 小时后再试
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();  // 恢复中断状态
                            }
                        }
                    }
                }
            });
            String userStr;
                // 创建 ObjectMapper 实例
                ObjectMapper objectMapper = new ObjectMapper();

                // 序列化对象为 JSON 字符串
                userStr = objectMapper.writeValueAsString(userResult);



            //这里用mq创建钱包 并且每天都会用定时任务  查询钱包表的用户是否和用户表的一致  如果不一致的话 就解决问题
            return Result.success(userStr);
        } else {
            //占用
            return Result.error("用户名已被占用");
        }
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


    @Autowired
    private ObjectMapper objectMapper;  // 用于将对象转换为JSON字符串

    @Autowired
    private VideoService videoService;  // 用于将对象转换为JSON字符串
    @GetMapping("/userInfo")
    public MeInfo userInfo() {
        Object userId = StpUtil.getLoginId();
        System.out.println("当前用户的ID: " + userId);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);

        MeInfo meInfo=new MeInfo();
       User user=   userService.getOne(queryWrapper);
        LambdaQueryWrapper<Video> queryWrapperV = new LambdaQueryWrapper<>();
        queryWrapperV.eq(Video::getAuthId, userId);
List<Video> videoList=videoService.list(queryWrapperV);
// 使用 BeanUtils 复制相同属性
        BeanUtils.copyProperties(user, meInfo);
        meInfo.setVideoList(videoList);
       String useridStr= String.valueOf(userId);  // 将 user 对象转换为 JSON 字符串
//        List<Video> videoList = videoService.getVideosByUserId(user.getId());

        try {
            String userJson = objectMapper.writeValueAsString(user);

            String key=useridStr+"_user_info";
            stringRedisTemplate.opsForValue().set(key,userJson,10,TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return meInfo;
    }
//
//    @PutMapping("/update")
//    public Result update(@RequestBody @Validated User user) {
//        userService.update(user);
//        return Result.success();
//    }

//    @PatchMapping("updateAvatar")
//    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
//        userService.updateAvatar(avatarUrl);
//        return Result.success();
//    }
//
//    @PatchMapping("/updatePwd")
//    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token) {
//        //1.校验参数
//        String oldPwd = params.get("old_pwd");
//        String newPwd = params.get("new_pwd");
//        String rePwd = params.get("re_pwd");
//
//        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
//            return Result.error("缺少必要的参数");
//        }
//
//        //原密码是否正确
//        //调用userService根据用户名拿到原密码,再和old_pwd比对
//        Map<String,Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        User loginUser = userService.findByUserName(username);
//        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
//            return Result.error("原密码填写不正确");
//        }
//
//        //newPwd和rePwd是否一样
//        if (!rePwd.equals(newPwd)){
//            return Result.error("两次填写的新密码不一样");
//        }
//
//        //2.调用service完成密码更新
//        userService.updatePwd(newPwd);
//        //删除redis中对应的token
//        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        operations.getOperations().delete(token);
//        return Result.success();
//    }
}
