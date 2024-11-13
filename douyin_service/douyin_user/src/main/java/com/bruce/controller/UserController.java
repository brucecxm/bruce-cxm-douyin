package com.bruce.controller;

import com.bruce.dao.chatInfoDao;
import com.bruce.user.pojo.User;
import com.bruce.user.pojo.Result;
import com.bruce.service.UserService;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.Md5Util;
import com.bruce.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//
//
//    @Autowired
//    private OAuth2AuthorizedClientService authorizedClientService;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/loginqq")
//    public String login(@RegisteredOAuth2AuthorizedClient("qq") OAuth2AuthorizedClient authorizedClient) {
//        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
//        String userInfo = getUserInfo(accessToken.getTokenValue());
//        return userInfo;
//    }
//
//    private String getUserInfo(String accessToken) {
//        String url = "https://graph.qq.com/user/get_user_info?access_token=" + accessToken + "&oauth_consumer_key=YOUR_APP_ID";
//        return restTemplate.getForObject(url, String.class);
//    }
//





@Autowired
private chatInfoDao chatInfoDaoone;

    @GetMapping("/getuserinfo")
    public Result<User> getuserinfo(String user_id) {

     User user=   chatInfoDaoone.queryUserInfoone(user_id);

        return Result.success(user);
    }






    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {

        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //没有占用
            //注册
            userService.register(username, password);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用");
        }
    }

//    \\S: 代表一个非空白字符。它可以是字母、数字或其他符号，但不能是空格、制表符或其他空白字符。
//    {5,16}:
//            5 表示至少需要 5 个非空白字符。
//            16 表示最多只能有 16 个非空白字符。

    @PostMapping("/login")
    public Result<String> login(
            @Pattern(regexp = "^\\S{5,16}$") @RequestParam(name = "username", defaultValue = "admin", required = true) String username,
            @Pattern(regexp = "^\\S{5,16}$") String password) {

        // 根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        // 判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        String hashedPassword = Md5Util.getMD5String(password);
        boolean isEmail = true;  // 假设这里是一个标志，您可以根据实际情况调整
        String yanzhengma = "5657"; // 验证码

        // 检查密码
        if (isEmail) {
//            if (hashedPassword.equals(loginUser.getPassword())) {
//                return generateTokenAndStoreInRedis(loginUser);
//            } else {
//                return checkValidationCodeAndLogin(loginUser, yanzhengma);
//            }
            return Result.error(String.valueOf(loginUser.getId()));

        }

        return Result.error("密码错误");
    }

    private Result<String> generateTokenAndStoreInRedis(User loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginUser.getId());
        claims.put("username", loginUser.getUsername());
        String token = JwtUtil.genToken(claims);

        try {
            // 把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);
        } catch (RedisConnectionFailureException ex) {
            System.out.println("Redis 连接失败，请检查 Redis 服务器是否运行。");
        } catch (Exception ex) {
            System.out.println("Redis 连接失败，请检查 Redis 服务器是否运行。");
        }
token=loginUser.getUsername();
        return Result.success(token);
    }

    //核对验证码
    //核对验证码
    private Result<String> checkValidationCodeAndLogin(User loginUser, String yanzhengma) {
        String operations = stringRedisTemplate.opsForValue().get("yancode");
        if (operations != null && operations.equals(yanzhengma)) {
            return generateTokenAndStoreInRedis(loginUser);
        } else {
            return Result.error("密码错误");
        }
    }




    @Autowired
    private RabbitTemplate rabbitTemplate;


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
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        //根据用户名查询用户
       /* Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码,再和old_pwd比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写不正确");
        }

        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一样");
        }

        //2.调用service完成密码更新
        userService.updatePwd(newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
