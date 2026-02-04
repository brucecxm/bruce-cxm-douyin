package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.bruce.dto.*;
import com.bruce.entity.*;
import com.bruce.handlar.MyCustomException;
import com.bruce.pojo.Result;
import com.bruce.service.*;
import com.bruce.utils.JwtUtil;
import com.bruce.utils.RedisUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.redisson.api.RedissonClient;
import org.redisson.api.RBloomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.bruce.monitor.BloomFilterMonitor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private RBloomFilter<Long> userBloomFilter;

    @Autowired
    private BloomFilterMonitor bloomFilterMonitor;

    // 布隆过滤器配置
    @Value("${bloom-filter.warmup-enabled:true}")
    private boolean bloomFilterWarmupEnabled;

    @Value("${bloom-filter.require-warmup:true}")
    private boolean bloomFilterRequireWarmup;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SaltService saltService;

    @Autowired
    private UserService userService;

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody User user) {
        try {
            // 获取当前登录用户ID
            Long userId = StpUtil.getLoginIdAsLong();

            // 调用服务更新用户信息
            boolean success = userService.updateUserInfo(user, userId);
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    // 分页查询所有用户
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 调用服务获取用户列表
            IPage<User> userPage = userService.getUserList(page, pageSize);

            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", userPage.getRecords());
            data.put("total", userPage.getTotal());
            data.put("size", userPage.getSize());
            data.put("current", userPage.getCurrent());

            return Result.success(data);
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // 根据ID查询用户
    @GetMapping("/getById")
    public Result<User> getUserById(@RequestParam("id") Long id) {
        try {
            User user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success(user);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    // 测试调用douyin_video微服务
    @GetMapping("/testVideoFeign")
    public Result<Object> testVideoFeign(@RequestParam("id") Long id) {
        try {
            Object video = videoFeignService.getVideoById(id);
            return Result.success(video);
        } catch (Exception e) {
            log.error("调用视频服务失败", e);
            return Result.error("调用视频服务失败，请稍后重试");
        }
    }

    // 测试获取视频列表
    @GetMapping("/testVideoList")
    public Result<Object> testVideoList() {
        try {
            Object videoList = videoFeignService.getVideoList();
            return Result.success(videoList);
        } catch (Exception e) {
            log.error("调用视频服务失败", e);
            return Result.error("调用视频服务失败，请稍后重试");
        }
    }

    @Autowired
    private FriendService friendService;

    @Autowired
    private VideoFeignService videoFeignService;

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
    public Result assignRoles(@RequestBody UserRoleDTO dto) {
        userRoleService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.success("角色分配成功");
    }

    @GetMapping("/roles/{userId}")
    public Result<List<Role>> getRoles(@PathVariable Long userId) {
        return Result.success(roleService.getRolesByUserId(userId));
    }

    @GetMapping("/permissions/{userId}")
    public Result<List<Permission>> getPermissions(@PathVariable Long userId) {
        return Result.success(permissionService.getPermissionsByUserId(userId));
    }

    @PostMapping("logout")
    public void handleLogoutAction(@RequestBody LogoutRequestDTO request) {
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

    @PostMapping("/doLogin")
    @Transactional
    public Result doLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            // 参数校验
            if (loginRequestDTO.getUserName() == null || loginRequestDTO.getUserName().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            // 调用用户服务执行登录
            Map<String, Object> response = userService.login(loginRequestDTO);
            return Result.success(response);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 缓存穿透实例接口
    // todo 下面的就是 启动时通过多线程异步预热数据 预热完成前用把缓存设置为空的办法 预热完毕后在用布隆过滤器
    // todo 下面的就是 启动时通过多线程异步预热数据 预热完成前用把缓存设置为空的办法 预热完毕后在用布隆过滤器
    // todo 下面的就是 启动时通过多线程异步预热数据 预热完成前用把缓存设置为空的办法 预热完毕后在用布隆过滤器

    // ****************************************************************

    // todo 下面是通过脚本 比如python 或者别的 每天凌晨相当于手动把需要预热的数据写入redis
    // 性能优化方案：可通过离线任务预先生成布隆过滤器快照（如每日凌晨用 Spark/Flink 读取用户全量表，
    // 生成布隆过滤器并序列化存储到 Redis），服务启动时直接加载 Redis 中的快照数据，无需实时预热；
    // 同时在业务侧通过binlog 增量同步（如 Canal 监听用户表变更），将新增/删除的用户ID实时同步到布隆过滤器。
    @GetMapping("/userInfo")
    public Result<UserInfoResponseDTO> userInfo(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "isRedis", required = false) Boolean isRedis) {
        try {
            // 如果未提供userId，使用当前登录用户ID
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 验证userId格式
            if (userId <= 0) {
                return Result.error("用户ID格式错误，请输入有效的数字ID");
            }

            // 构建请求DTO
            UserInfoRequestDTO requestDTO = new UserInfoRequestDTO();
            requestDTO.setUserId(userId);
            requestDTO.setPage(page);
            requestDTO.setPageSize(pageSize);
            requestDTO.setType(type);
            requestDTO.setRedis(isRedis != null && isRedis);

            // 调用服务获取用户信息
            UserInfoResponseDTO userInfo = userService.getUserInfo(requestDTO);

            if (userInfo == null) {
                return Result.error("用户不存在");
            }

            return Result.success(userInfo);
        } catch (NumberFormatException e) {
            return Result.error("用户ID格式错误");
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败，请稍后重试");
        }
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
    public void getEmailVerificationCodeService(@RequestBody Map<String, Object> params) throws IOException {
        // 1. 提取参数
        String email = String.valueOf(params.getOrDefault("email", ""));
        if (email.isEmpty()) {
            throw new RuntimeException("邮箱不能为空");
        }
        // 2. 生成验证码
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        // 3. 存入 Redis，有效期 5 分钟
        stringRedisTemplate.opsForValue().set("email:code:" + email, code, 5, TimeUnit.MINUTES);
        // 4. 构造消息
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("code", code);
        // 5. 发送到队列（work 模型只需一个队列）
        rabbitTemplate.convertAndSend("email.queue", message);
    }

    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    // 简单的参数类型 如果没有用参数注解 spring会默认把参数前面加上@RequestParam
    @PostMapping("/register")
    @Transactional
    public Result register(@RequestBody Map<String, Object> params) {
        // 1. 参数提取
        String userName = String.valueOf(params.getOrDefault("userName", ""));
        String password = String.valueOf(params.getOrDefault("password", ""));
        boolean isCheck = Boolean.parseBoolean(String.valueOf(params.getOrDefault("isCheck", "false")));
        String verificationCode = String.valueOf(params.getOrDefault("verificationCode", ""));

        // 2. 调用服务进行注册
        try {
            User user = userService.register(userName, password, isCheck, verificationCode);
            return Result.success(user);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (MyCustomException e) {
            log.error("业务异常: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常: {}", e.getMessage(), e);
            return Result.error("注册失败，请稍后重试");
        }
    }

    // ===== 一、用户基础信息扩展接口 =====

    // 更新用户信息
    @PutMapping("/updateInfo")
    @Transactional
    public Result updateInfo(@RequestBody UpdateInfoRequestDTO request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 调用服务更新用户信息
            boolean success = userService.updateInfo(
                    userId,
                    request.getNickname(),
                    request.getAvatar(),
                    request.getPhone(),
                    request.getGender());

            if (success) {
                return Result.success(null);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    // 修改密码
    @PostMapping("/changePassword")
    @Transactional
    public Result changePassword(@RequestBody ChangePasswordRequestDTO request) {
        try {
            Long userId = request.getUserId();
            // 如果未指定userId，使用当前登录用户
            if (userId == null) {
                userId = Long.parseLong(StpUtil.getLoginId().toString());
            }

            // 调用服务修改密码
            boolean success = userService.changePassword(
                    userId,
                    request.getOldPassword(),
                    request.getNewPassword(),
                    request.getVerificationCode());

            if (success) {
                return Result.success(null);
            } else {
                return Result.error("用户不存在");
            }
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.error("修改失败，请稍后重试");
        }
    }

    // ===== 二、用户状态与安全控制接口 =====

    // 查询用户状态
    @GetMapping("/status/{userId}")
    public Result<UserStatusResponseDTO> getUserStatus(@PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 这里简化处理，实际应根据用户状态字段返回
            // 模拟返回正常状态
            UserStatusResponseDTO response = new UserStatusResponseDTO(1, "");
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询用户状态失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }
}