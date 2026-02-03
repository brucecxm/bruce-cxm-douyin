package com.bruce.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.http.Method;
import com.bruce.dict.Constant;
import com.bruce.dto.LikeMessage;
import com.bruce.entity.*;
import com.bruce.service.LocationService;
import com.bruce.service.PrivacyService;
import com.bruce.service.TopicService;
import com.bruce.file.service.FileStorageService;
import com.bruce.pojo.Result;
import com.bruce.service.*;
//import com.bruce.MusicClient;
//import com.bruce.UserClient;
import com.bruce.vo.VideoVO;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * (Video)表控制层
 *
 * @author makejava
 * @since 2024-06-25 13:03:23
 */
@RestController
@RequestMapping("/video")
public class VideoController extends ApiController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private VideoService videoService;

    @Autowired
    private VideoInfoService videoInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private MessageQueueService messageQueueService;

    @Autowired
    private IdService idService;

    @Autowired
    private FileStorageService fileStorageService;

    @Resource
    private UserService userService;

    @Resource
    private MusicService musicService;

    @Resource
    private LikeService likeService;

    @Resource
    private FavorService favorService;
    @Resource
    private CommentService commentService;

    @PostMapping("/upvideo")
    public ResponseEntity<?> upvideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> params) {
        StpUtil.checkLogin(); // 检查是否登录（自动从请求头拿 token，去 Redis 查）
        Object loginId = StpUtil.getLoginId(); // 获取当前登录用户 ID
        try {
            // 1. 参数处理
            String videoTitle = params.get("videoTitle");
            String videoContext = params.get("videoContext");
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("上传失败：文件不能为空");
            }
            // 2. 模拟写死的数据
            String authId = "100112";
            String videoId = String.valueOf(idService.SnowflakeGen());
            String musicId = "100112";
            // 3. 拼接文件地址（MinIO上传部分略）
            String fileName = file.getOriginalFilename(); // 更推荐使用 getOriginalFilename
            String videoUrl = "http://192.168.200.130:9000/mybucket/" + fileName;
            // 4. 构造 video 实体
            Video video = new Video();
            video.setVideoId(Long.parseLong(videoId));
            video.setContent(videoContext);
            video.setUrl(videoUrl);
            video.setUserId(Long.valueOf(authId));
            video.setMusicId(Long.valueOf(musicId));
            video.setTitle(videoTitle);
            InputStream in = file.getInputStream();
            fileStorageService.uploadImgFile(videoId, videoId, in);
            // 5. 保存到数据库
            videoService.save(video);
            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            e.printStackTrace(); // 控制台输出异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器错误：" + e.getMessage());
        }
    }

    @Autowired
    private DcSystemService dcSystemService;

    @PostMapping("/publish")
    public ResponseEntity<?> publishVideo(
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam("title") String title,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "privacy", defaultValue = "公开") String privacy,
            @RequestParam(value = "metadata", required = false) String metadataJson,
            @RequestParam("createdAt") String createdAt) throws IOException {

        String fileName = videoFile.getOriginalFilename();
        Object userIdObj = StpUtil.getLoginId();
        System.out.println(StpUtil.isLogin());

        String videoId = String.valueOf(idService.SnowflakeGen());
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println("当前操作系统: " + osName);
        String minio = "/minio";
        LambdaQueryWrapper<DcSystem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DcSystem::getSysName, "ngrok");
        DcSystem ngrok = dcSystemService.getOne(queryWrapper);

        // 获取文件内容类型，用于判断是视频还是图片
        String contentType = videoFile.getContentType();

        // 使用try-with-resources语句，确保输入流自动关闭
        String url;
        try (InputStream in = videoFile.getInputStream()) {
            // 使用用户提供的MinIO bucket: douyin
            url = fileStorageService.uploadVideoFile("douyin", fileName, in);
        }

        // 获取登录用户ID
        Long userId = null;
        if (userIdObj instanceof Long) {
            userId = (Long) userIdObj;
        } else if (userIdObj instanceof Integer) {
            userId = ((Integer) userIdObj).longValue();
        } else if (userIdObj instanceof String) {
            userId = Long.parseLong((String) userIdObj);
        } else {
            // 处理其他类型或null的情况
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("code", 401));
        }

        Video video = new Video();
        video.setVideoId(idService.SnowflakeGen());
        video.setUrl(url);

        // 根据文件类型设置视频类型：1=视频，2=图片
        int type = contentType.startsWith("image/") ? 2 : 1;
        video.setType(type);

        video.setTitle(title);
        video.setContent("");
        video.setUserId(userId);

        // 处理缩略图上传
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailFileName = "thumbnail_" + video.getVideoId() + "_" + System.currentTimeMillis() + ".jpg";
            try (InputStream thumbnailIn = thumbnail.getInputStream()) {
                String thumbnailUrl = fileStorageService.uploadVideoFile("douyin", thumbnailFileName, thumbnailIn);
                video.setCover(thumbnailUrl);
            }
        } else if (type == 2) {
            // 图片类型，封面为图片本身
            video.setCover(url);
        }

        videoService.save(video);
        return ResponseEntity.ok(Collections.singletonMap("code", 200));
    }

    @PostMapping("/publishss")
    public ResponseEntity<?> publish(@RequestBody Map<String, Object> req) {
        Object userId = StpUtil.getLoginId();
        Object title = req.get("title");
        Object musicId = req.get("musicId");
        Object content = req.get("content");
        Object type = req.get("type");
        Object status = req.get("status");
        if (userId == null || StringUtils.isEmpty(userId.toString())
                || title == null || StringUtils.isEmpty(title.toString())) {
            return ResponseEntity.badRequest().body("缺少必要字段");
        }
        Video video = new Video();
        video.setCover("1");
        video.setTitle(String.valueOf(req.get("title")));
        video.setVideoId(idService.SnowflakeGen());
        video.setMusicId((Long) musicId);
        video.setContent((String) content);
        video.setType((Integer) type);
        video.setStatus((Integer) status);
        videoService.save(video);
        return ResponseEntity.ok(Collections.singletonMap("code", 200));
    }

    @GetMapping("/getVideoPage")
    public List<VideoVO> getVideoInfo(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码page不能小于1") // 校验页码：最小值1
            int page,

            @RequestParam(defaultValue = "4") @Min(value = 1, message = "每页条数size不能小于1") // 校验条数：最小值1
            int size,

            @RequestParam @NotBlank(message = "视频类型videoType不能为空且不能为空白字符") // 校验非空+非空白
            String videoType) {

        return videoService.getVideoPage(page, size, videoType);
    }

    @GetMapping("/auth")
    public Map<String, Object> getAuth(
            @RequestParam(required = false) Integer userid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Object UserIdObj = StpUtil.getLoginId();
        Long loginUserId = UserIdObj != null ? Long.parseLong(UserIdObj.toString()) : 0L;
        if (userid == null) {
            userid = loginUserId.intValue(); // 使用当前登录用户ID
        }
        Map<String, Object> result = new HashMap<>();
        // 查询用户信息
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getId, userid);
        User userInfo = userService.getOne(userQuery);
        result.put(Constant.User.USER_INFO, userInfo);
        // 查询视频分页数据
        Page<Video> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getUserId, userid);
        IPage<Video> videoPage = videoService.page(pageInfo, videoQuery);
        result.put(Constant.Video.VIDEO_PAGE_LIST, videoPage.getRecords());
        result.put(Constant.Video.TOTAL, videoPage.getTotal());
        return result;
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> like(@RequestBody LikeRequest likeRequest) {
        try {
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 401);
            response.put("message", "用户未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());

        // 限流逻辑：每个用户每秒最多允许10次点赞操作
        String rateLimitKey = "rate:limit:like:" + userId;
        long current = System.currentTimeMillis() / 1000;
        redisTemplate.opsForHash().put(rateLimitKey, "last", current);
        redisTemplate.expire(rateLimitKey, 1, java.util.concurrent.TimeUnit.SECONDS);
        Long count = redisTemplate.opsForHash().increment(rateLimitKey, "count", 1);
        if (count > 10) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 429);
            response.put("message", "点赞操作过于频繁，请稍后再试");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }

        // 从请求对象中获取参数
        Long contentId = likeRequest.getContentId();
        String type = likeRequest.getType();
        String contentType = likeRequest.getContentType();

        // 生成 Redis 键
        String redisKey = "like:content:" + contentType + contentId;
        String batchKey = "like:batch:" + contentType;

        // 处理点赞和取消点赞逻辑
        boolean isLiked = redisTemplate.opsForSet().isMember(redisKey, userId);
        String operation;
        String resultMessage;
        boolean success = false;

        if ("red".equals(type)) {
            // 点赞操作
            if (!isLiked) {
                // 将用户 ID 存入 Redis，表示该用户已点赞
                redisTemplate.opsForSet().add(redisKey, userId);
                // 将点赞数加 1
                redisTemplate.opsForValue().increment(redisKey + ":count", 1);
                operation = "add";
                resultMessage = contentType + "点赞成功";
                success = true;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 400);
                response.put("message", "您已点赞");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            // 取消点赞操作
            if (isLiked) {
                // 将用户 ID 从 Redis 中移除，表示取消点赞
                redisTemplate.opsForSet().remove(redisKey, userId);
                // 将点赞数减 1
                redisTemplate.opsForValue().decrement(redisKey + ":count", 1);
                operation = "del";
                resultMessage = contentType + "取消点赞成功";
                success = true;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 400);
                response.put("message", "您尚未点赞");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // 优化：将点赞消息先存入Redis列表，而非直接发送到MQ
        if (success) {
            LikeMessage message = new LikeMessage(contentId, userId, operation, System.currentTimeMillis());
            redisTemplate.opsForList().rightPush(batchKey, message);

            // 检查是否达到批量发送阈值
            Long size = redisTemplate.opsForList().size(batchKey);
            if (size >= 100) { // 每100条消息批量发送一次
                // 批量发送消息到MQ
                List<LikeMessage> messages = redisTemplate.opsForList().range(batchKey, 0, 99);
                if (messages != null && !messages.isEmpty()) {
                    // 设置消息持久化
                    rabbitTemplate.convertAndSend("like.exchange", "like.video", messages, msg -> {
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    });
                    // 删除已发送的消息
                    redisTemplate.opsForList().trim(batchKey, 100, -1);
                }
            }
        }

        // 获取最新的点赞数
        Integer likeCount = (Integer) redisTemplate.opsForValue().get(redisKey + ":count");
        if (likeCount == null) {
            // 如果Redis中没有点赞数，从数据库查询
            LambdaQueryWrapper<Favor> likeQueryWrapper = new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, contentId);
            likeCount = favorService.count(likeQueryWrapper);
            // 将查询结果存入Redis
            redisTemplate.opsForValue().set(redisKey + ":count", likeCount);
        }

        // 返回统一格式的响应
        Map<String, Object> data = new HashMap<>();
        data.put("likeCount", likeCount);
        data.put("isLiked", "red".equals(type) ? true : false);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", resultMessage);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/collect")
    public ResponseEntity<Map<String, Object>> collect(@RequestBody LikeRequest likeRequest) {
        try {
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 401);
            response.put("message", "用户未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());

        // 限流逻辑：每个用户每秒最多允许10次收藏操作
        String rateLimitKey = "rate:limit:collect:" + userId;
        long current = System.currentTimeMillis() / 1000;
        redisTemplate.opsForHash().put(rateLimitKey, "last", current);
        redisTemplate.expire(rateLimitKey, 1, java.util.concurrent.TimeUnit.SECONDS);
        Long count = redisTemplate.opsForHash().increment(rateLimitKey, "count", 1);
        if (count > 10) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 429);
            response.put("message", "收藏操作过于频繁，请稍后再试");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }

        // 从请求对象中获取参数
        Long contentId = likeRequest.getContentId();
        String type = likeRequest.getType();
        String contentType = likeRequest.getContentType();

        // 生成 Redis 键
        String redisKey = "collect:content:" + contentType + contentId;
        String batchKey = "collect:batch:" + contentType;

        // 处理收藏和取消收藏逻辑
        boolean isCollected = redisTemplate.opsForSet().isMember(redisKey, userId);
        String operation;
        String resultMessage;
        boolean success = false;

        if ("uncollect".equals(type)) {
            // 取消收藏操作
            if (isCollected) {
                // 将用户 ID 从 Redis 中移除，表示取消收藏
                redisTemplate.opsForSet().remove(redisKey, userId);
                // 将收藏数减 1
                redisTemplate.opsForValue().decrement(redisKey + ":count", 1);
                operation = "del";
                resultMessage = contentType + "取消收藏成功";
                success = true;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 400);
                response.put("message", "您尚未收藏");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            // 收藏操作
            if (!isCollected) {
                // 将用户 ID 存入 Redis，表示该用户已收藏
                redisTemplate.opsForSet().add(redisKey, userId);
                // 将收藏数加 1
                redisTemplate.opsForValue().increment(redisKey + ":count", 1);
                operation = "add";
                resultMessage = contentType + "收藏成功";
                success = true;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 400);
                response.put("message", "您已收藏");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // 优化：将收藏消息先存入Redis列表，而非直接发送到MQ
        if (success) {
            LikeMessage message = new LikeMessage(contentId, userId, operation, System.currentTimeMillis());
            redisTemplate.opsForList().rightPush(batchKey, message);

            // 检查是否达到批量发送阈值
            Long size = redisTemplate.opsForList().size(batchKey);
            if (size >= 100) { // 每100条消息批量发送一次
                // 批量发送消息到MQ
                List<LikeMessage> messages = redisTemplate.opsForList().range(batchKey, 0, 99);
                if (messages != null && !messages.isEmpty()) {
                    // 设置消息持久化
                    rabbitTemplate.convertAndSend("like.exchange", "like.video", messages, msg -> {
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    });
                    // 删除已发送的消息
                    redisTemplate.opsForList().trim(batchKey, 100, -1);
                }
            }
        }

        // 获取最新的收藏数
        Integer collectCount = (Integer) redisTemplate.opsForValue().get(redisKey + ":count");
        if (collectCount == null) {
            // 如果Redis中没有收藏数，从数据库查询
            LambdaQueryWrapper<Favor> collectQueryWrapper = new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, contentId);
            collectCount = favorService.count(collectQueryWrapper);
            // 将查询结果存入Redis
            redisTemplate.opsForValue().set(redisKey + ":count", collectCount);
        }

        // 返回统一格式的响应
        Map<String, Object> data = new HashMap<>();
        data.put("collectCount", collectCount);
        data.put("isCollected", "uncollect".equals(type) ? false : true);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", resultMessage);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 点赞请求对象
    static class LikeRequest {
        private Long contentId;
        private String type;
        private String contentType;

        // getter and setter methods
        public Long getContentId() {
            return contentId;
        }

        public void setContentId(Long contentId) {
            this.contentId = contentId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    @Autowired
    private MinioClient minioClient;

    @Resource
    private TopicService topicService;

    @Resource
    private LocationService locationService;

    @Resource
    private PrivacyService privacyService;

    @Autowired
    private UserFeignService userFeignService;

    private final String BUCKET = "douyin";

    // 新增：定义签名URL的有效期（单位：秒），这里设置1小时（3600秒）
    private static final int URL_EXPIRE_SECONDS = 3600;

    @GetMapping("/play/{videoId}")
    public Result getPlayInfo(@PathVariable Long videoId) {
        // 1. 查询数据库获取视频信息
        VideoInfo videoInfo = videoInfoService.getById(videoId);
        if (videoInfo == null) {
            return Result.error("视频不存在");
        }
        if (videoInfo.getStatus() != 1) {
            return Result.error("视频正在转码中，暂无法播放");
        }

        // 2. 生成带签名的.m3u8临时访问URL
        try {
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(BUCKET)
                            .object(videoInfo.getM3u8MinioPath())
                            .method(Method.GET)
                            .expiry(URL_EXPIRE_SECONDS)
                            .build());

            // 3. 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("videoId", videoId);
            data.put("title", videoInfo.getVideoTitle());
            data.put("duration", videoInfo.getDuration());
            data.put("fileSize", videoInfo.getFileSize());
            data.put("m3u8Url", presignedUrl);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("生成播放链接失败");
        }
    }

    // @GetMapping("/one")
    // public R getone() {
    // List<Video> videos = this.videoService.list();
    // System.out.println("one");
    //
    // List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
    // for (Video video : videos) {
    // VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
    // // 复制 Video 对象的属性到 VideoWithAvatar 对象
    // BeanUtils.copyProperties(video, videoWithAvatar);
    // Map<String, String> map = ThreadLocalUtil.get();
    //// todo 远程调用用户和音乐信息 后面异常要加到全局异常处理器 这里的异常需要更细节的处理 下面more接口还有一个异常
    // try {
    // String avatar = userClient.avatar(video.getAuthId(),
    // map.get("Authorization")).getUserPic();
    // videoWithAvatar.setAvatar(avatar); // 这是第60行
    //
    // String nickname = userClient.avatar(video.getAuthId(),
    // map.get("Authorization")).getNickname();
    // videoWithAvatar.setNickname(nickname); // 这是第60行
    //
    // String musicAvatar = musicClient.avatar(video.getMusicId());
    // videoWithAvatar.setMusicAvatar(musicAvatar); //
    // videosWithAvatar.add(videoWithAvatar);
    //
    // } catch (Exception e) {
    // System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /one接口");
    //
    // }
    // }
    // return success(videosWithAvatar);
    // }

    // @GetMapping("/more")
    // public R getmore() {
    // List<Video> videos = this.videoService.list();
    // System.out.println("more");
    //
    // List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
    // for (Video video : videos) {
    // VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
    // // 复制 Video 对象的属性到 VideoWithAvatar 对象
    // BeanUtils.copyProperties(video, videoWithAvatar);
    // Map<String, String> map = ThreadLocalUtil.get();
    // try {
    // String avatar = userClient.avatar(video.getAuthId(),
    // map.get("Authorization")).getUserPic();
    // videoWithAvatar.setAvatar(avatar); // 这是第60行
    //
    // String nickname = userClient.avatar(video.getAuthId(),
    // map.get("Authorization")).getNickname();
    // videoWithAvatar.setNickname(nickname); // 这是第60行
    //
    // String musicAvatar = musicClient.avatar(video.getMusicId());
    // videoWithAvatar.setMusicAvatar(musicAvatar); //
    // videosWithAvatar.add(videoWithAvatar);
    //
    // } catch (Exception e) {
    // System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /more接口");
    //
    // }
    //
    //
    // }
    // return success(videosWithAvatar);
    // }

    /**
     * 获取话题列表
     *
     * @return 话题列表
     */
    @GetMapping("/topic/list")
    public ResponseEntity<?> getTopics() {
        try {
            List<Topic> topics = topicService.getAllTopics();
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "获取话题列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取位置列表
     *
     * @return 位置列表
     */
    @GetMapping("/location/list")
    public ResponseEntity<?> getLocations() {
        try {
            List<Location> locations = locationService.getAllLocations();
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "获取位置列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取权限设置列表
     *
     * @return 权限设置列表
     */
    @GetMapping("/privacy/list")
    public ResponseEntity<?> getPrivacies() {
        try {
            List<Privacy> privacies = privacyService.getAllPrivacies();
            return ResponseEntity.ok(privacies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "获取权限设置列表失败: " + e.getMessage()));
        }
    }

    /**
     * 获取搜索建议
     *
     * @param keyword 搜索关键词
     * @return 搜索建议列表
     */
    @GetMapping("/search/suggestions")
    public ResponseEntity<?> getSearchSuggestions(@RequestParam String keyword) {
        try {
            // 这里可以根据实际需求从数据库或缓存中获取搜索建议
            // 暂时返回模拟数据
            List<String> suggestions = new ArrayList<>();
            suggestions.add(keyword + " 热门");
            suggestions.add(keyword + " 最新");
            suggestions.add(keyword + " 搞笑");
            suggestions.add(keyword + " 教程");
            suggestions.add(keyword + " 美食");

            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "获取搜索建议失败: " + e.getMessage()));
        }
    }

    /**
     * 获取热门搜索
     *
     * @return 热门搜索列表
     */
    @GetMapping("/search/hot")
    public ResponseEntity<?> getHotSearches() {
        try {
            // 这里可以根据实际需求从数据库或缓存中获取热门搜索
            // 暂时返回模拟数据
            List<Map<String, Object>> hotSearches = new ArrayList<>();

            Map<String, Object> search1 = new HashMap<>();
            search1.put("keyword", "热门视频");
            search1.put("trending", "↑ 123");
            hotSearches.add(search1);

            Map<String, Object> search2 = new HashMap<>();
            search2.put("keyword", "最新音乐");
            search2.put("trending", "↑ 45");
            hotSearches.add(search2);

            Map<String, Object> search3 = new HashMap<>();
            search3.put("keyword", "搞笑段子");
            search3.put("trending", "↓ 67");
            hotSearches.add(search3);

            Map<String, Object> search4 = new HashMap<>();
            search4.put("keyword", "美食教程");
            search4.put("trending", "↑ 89");
            hotSearches.add(search4);

            Map<String, Object> search5 = new HashMap<>();
            search5.put("keyword", "旅游攻略");
            search5.put("trending", "↑ 32");
            hotSearches.add(search5);

            return ResponseEntity.ok(hotSearches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "获取热门搜索失败: " + e.getMessage()));
        }
    }

    /**
     * 搜索视频
     *
     * @param keyword 搜索关键词
     * @param page    页码
     * @param size    每页数量
     * @return 搜索结果列表
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Video> pageInfo = new Page<>(page, size);

            // 根据关键词搜索视频，匹配标题和内容
            LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.and(wrapper -> wrapper
                    .like(Video::getTitle, keyword)
                    .or().like(Video::getContent, keyword))
                    .orderByDesc(Video::getVideoId);

            IPage<Video> videoPage = videoService.page(pageInfo, queryWrapper);
            List<Video> videos = videoPage.getRecords();

            List<Map<String, Object>> result = new ArrayList<>();

            // 处理搜索结果，添加用户信息和统计数据
            for (Video video : videos) {
                VideoVO vo = new VideoVO();
                BeanUtils.copyProperties(video, vo);

                // 用户信息
                User user = userService.getById(video.getUserId());
                if (user != null) {
                    vo.setUsername(user.getNickname());
                    vo.setUserAvatar(user.getAvatar());
                }

                // 音乐信息
                Music music = musicService.getById(video.getMusicId());
                if (music != null) {
                    vo.setMusic(music.getMusicName());
                    vo.setMusicAvatar(music.getMusicAvatar());
                    vo.setMusicId(music.getId());
                }

                // 点赞数
                String redisKey = "like:content:" + "video" + video.getVideoId();
                Integer likenum = (Integer) redisTemplate.opsForValue().get(redisKey + ":count");
                int likeCount = likenum != null ? likenum : 0;
                vo.setLikeNum((long) likeCount);

                // 评论数
                LambdaQueryWrapper<Comment> commentQueryWrapper = new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCommentId, video.getVideoId())
                        .isNotNull(Comment::getComment);
                int commentCount = commentService.count(commentQueryWrapper);
                vo.setCommentNum((long) commentCount);

                // 分享数（暂时使用模拟数据）
                vo.setShareNum(0L);

                // 组装返回数据
                Map<String, Object> map = new HashMap<>();
                map.put("videoId", vo.getVideoId());
                map.put("title", vo.getTitle());
                map.put("content", vo.getContent());
                map.put("url", vo.getUrl());
                map.put("cover", vo.getCover());
                map.put("username", vo.getUsername());
                map.put("likeNum", vo.getLikeNum());
                map.put("commentNum", vo.getCommentNum());
                map.put("shareNum", vo.getShareNum());
                result.add(map);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "搜索视频失败: " + e.getMessage()));
        }
    }

    // 根据ID查询视频
    @GetMapping("/getById")
    public Result<Video> getVideoById(@RequestParam("id") Long id) {
        try {
            Video video = videoService.getById(id);
            if (video == null) {
                return Result.error("视频不存在");
            }
            return Result.success(video);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询视频失败，请稍后重试");
        }
    }

    // 获取视频列表
    @GetMapping("/list")
    public Result<Map<String, Object>> getVideoList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 使用MyBatis Plus的分页功能查询所有视频
            Page<Video> pageQuery = new Page<>(page, pageSize);
            IPage<Video> videoPage = videoService.page(pageQuery);

            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("records", videoPage.getRecords());
            data.put("total", videoPage.getTotal());
            data.put("size", videoPage.getSize());
            data.put("current", videoPage.getCurrent());

            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询视频列表失败，请稍后重试");
        }
    }

    // 测试调用douyin_user微服务
    @GetMapping("/testUserFeign")
    public Result<Object> testUserFeign(@RequestParam("id") Long id) {
        try {
            Object user = userFeignService.getUserById(id);
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("调用用户服务失败，请稍后重试");
        }
    }

    // 测试获取用户列表
    @GetMapping("/testUserList")
    public Result<Object> testUserList() {
        try {
            Object userList = userFeignService.getUserList();
            return Result.success(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("调用用户服务失败，请稍后重试");
        }
    }

    // 根据用户ID获取视频列表
    @GetMapping("/listByUserId")
    public Result<List<Video>> listByUserId(@RequestParam("userId") Long userId) {
        try {
            List<Video> videos = videoService.list(new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId));
            return Result.success(videos);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询视频列表失败，请稍后重试");
        }
    }

    // 根据用户ID分页获取视频列表
    @GetMapping("/pageByUserId")
    public Result<IPage<Video>> pageByUserId(@RequestParam("userId") Long userId, @RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        try {
            Page<Video> pageQuery = new Page<>(page, pageSize);
            IPage<Video> videoPage = videoService.page(pageQuery, new LambdaQueryWrapper<Video>().eq(Video::getUserId, userId));
            return Result.success(videoPage);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("分页查询视频列表失败，请稍后重试");
        }
    }
}
