package com.bruce.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bruce.entity.User;
import com.bruce.pojo.Result;
import com.bruce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {


    //*******************sa-token***********************
    @Autowired
private  UserService userService;

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



    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public Result<String> doLogin(String username, String password) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        // 查询符合条件的单条记录
        User user = userService.getOne(queryWrapper);


        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        // 获取当前会话的 token 值
       String token= StpUtil.getTokenValue();
        if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
            StpUtil.login(user.getId(),"PC");
            StpUtil.getSession().set("user", user);
            System.out.println("sss");
            return Result.success("sssssssssssssssssssssssssssssss");
        }
        return  Result.error("登录失败");
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public Result isLogin() {

        // 获取：当前账号所拥有的权限集合
      List<String> permission=  StpUtil.getPermissionList();

        for (String permissiontemp : permission
        ){
            if (permissiontemp.equals("user.get")
            ) {



// 判断：当前账号是否拥有指定角色, 返回 true 或 false
                if( StpUtil.hasRole("super-admin"))
                {
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

                    return   Result.success("当前会话是否登录：" + StpUtil.isLogin())            ;
                }

            }
        }

        return Result.error("无权查看");

    }













    //*******************sa-token***********************









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



//
//
//@Autowired
//private chatInfoDao chatInfoDaoone;
//
//    @GetMapping("/getuserinfo")
//    public Result<User> getuserinfo(String user_id) {
//
//     User user=   chatInfoDaoone.queryUserInfoone(user_id);
//
//        return Result.success(user);
//    }






//
//
//    @PostMapping("/register")
//    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
//
//        //查询用户
//        User u = userService.findByUserName(username);
//        if (u == null) {
//            //没有占用
//            //注册
//            userService.register(username, password);
//            return Result.success();
//        } else {
//            //占用
//            return Result.error("用户名已被占用");
//        }
//    }

//    \\S: 代表一个非空白字符。它可以是字母、数字或其他符号，但不能是空格、制表符或其他空白字符。
//    {5,16}:
//            5 表示至少需要 5 个非空白字符。
//            16 表示最多只能有 16 个非空白字符。

//    @PostMapping("/login")
//    public Result<String> login(
//            @Pattern(regexp = "^\\S{5,16}$") @RequestParam(name = "username", defaultValue = "admin", required = true) String username,
//            @Pattern(regexp = "^\\S{5,16}$") String password) {
//
//        // 根据用户名查询用户
//        User loginUser = userService.findByUserName(username);
//        // 判断该用户是否存在
//        if (loginUser == null) {
//            return Result.error("用户名错误");
//        }
//
//        String hashedPassword = Md5Util.getMD5String(password);
//        boolean isEmail = true;  // 假设这里是一个标志，您可以根据实际情况调整
//        String yanzhengma = "5657"; // 验证码
//
//        // 检查密码
//        if (isEmail) {
////            if (hashedPassword.equals(loginUser.getPassword())) {
////                return generateTokenAndStoreInRedis(loginUser);
////            } else {
////                return checkValidationCodeAndLogin(loginUser, yanzhengma);
////            }
//            return Result.error(String.valueOf(loginUser.getId()));
//
//        }
//
//        return Result.error("密码错误");
//    }
//
//    private Result<String> generateTokenAndStoreInRedis(User loginUser) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", loginUser.getId());
//        claims.put("username", loginUser.getUsername());
//        String token = JwtUtil.genToken(claims);
//
//        try {
//            // 把token存储到redis中
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            operations.set(token, token, 1, TimeUnit.HOURS);
//        } catch (RedisConnectionFailureException ex) {
//            System.out.println("Redis 连接失败，请检查 Redis 服务器是否运行。");
//        } catch (Exception ex) {
//            System.out.println("Redis 连接失败，请检查 Redis 服务器是否运行。");
//        }
//token=loginUser.getUsername();
//        return Result.success(token);
//    }

//    //核对验证码
//    //核对验证码
//    private Result<String> checkValidationCodeAndLogin(User loginUser, String yanzhengma) {
//        String operations = stringRedisTemplate.opsForValue().get("yancode");
//        if (operations != null && operations.equals(yanzhengma)) {
//            return generateTokenAndStoreInRedis(loginUser);
//        } else {
//            return Result.error("密码错误");
//        }
//    }




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
