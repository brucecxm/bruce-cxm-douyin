package com.bruce.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.entity.User;
import com.bruce.pojo.Result;
import com.bruce.service.UserService;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private JwtUtil jwtUtil ;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedisUtil redisUtil;


    //*******************sa-token***********************
    @Autowired
    private UserService userService;

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




    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public Result doLogin(String username, String password) throws Exception {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        // 查询符合条件的单条记录
        User user = userService.getOne(queryWrapper);

if(user==null) {
    return Result.error("登录失败");

}
        // 存储密码的哈希和盐值
        String storedPassword = hashPassword(user.getPassword());
        String[] parts = storedPassword.split(":");
        String storedHash = parts[0];
        String storedSalt = parts[1];

        // 从存储的哈希和盐值中恢复盐
        byte[] salt = Base64.getDecoder().decode(storedSalt);

        // 重新计算输入密码的哈希值
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        // 比较哈希值
        String inputHash = Base64.getEncoder().encodeToString(hash);
        boolean is_password= inputHash.equals(storedHash);

        if (user.getUsername().equals(username) ) {


// 获取当前会话是否已经登录，返回true=已登录，false=未登录
            StpUtil.login(2);

            String key= "user_login"+ String.valueOf(user.getId());
// 获取当前会话的 token 值
      String token_login= StpUtil.getTokenValue();

//            redisUtil.set(key,token_login);

            String token = jwtUtil.generateToken(user.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);


            return Result.success(response);
        }
        return Result.error("登录失败");
    }


    // Get mapping for test order selection
    @GetMapping("/testaa")
    @ResponseBody  // Adding @ResponseBody to return a response body
    public String OrderSelect() {
        // 会话登录：参数填写要登录的账号id，建议的数据类型：long | int | String， 不可以传入复杂类型，如：User、Admin 等等
        StpUtil.login(1);

        return "Order method executed successfully!"; // Returning a success message
    }


    //*******************sa-token***********************


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    @PostMapping("/register")
    public Result register(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User u = userService.getOne(queryWrapper); // 获取用户名对应的用户

        User user = new User();
        // 生成一个16字节的随机盐值
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        // 使用密码、盐值、迭代次数（10000）以及输出长度（256位）来生成哈希
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);

        // 使用 HMAC-SHA256 算法生成密钥
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        String passwordjie= Base64.getEncoder().encodeToString(hash);



        user.setUsername(username);
        user.setPassword(passwordjie);
        if (u == null) {
            //没有占用
            //注册
            userService.save(user);
            amqpTemplate.convertAndSend("create_walletone_exampleExchange", "create_walletone_RoutingKey", user.getId());
            //这里用mq创建钱包 并且每天都会用定时任务  查询钱包表的用户是否和用户表的一致  如果不一致的话 就解决问题
            return Result.success();
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
//
//
//
//    @GetMapping("/userInfo")
//    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
//        //根据用户名查询用户
//       /* Map<String, Object> map = JwtUtil.parseToken(token);
//        String username = (String) map.get("username");*/
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        User user = userService.findByUserName(username);
//        return Result.success(user);
//    }
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
