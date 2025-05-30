package com.bruce.controller;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dao.VideoOneDao;
import com.bruce.dto.LikeMessage;
import com.bruce.entity.*;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.*;
//import com.bruce.MusicClient;
//import com.bruce.UserClient;
import com.bruce.vo.VideoVO;
import io.swagger.annotations.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Video)表控制层
 *
 * @author makejava
 * @since 2024-06-25 13:03:23
 */
@Api(tags = "视频接口") // 给 Controller 标注一个分类
@RestController
@RequestMapping("/video")
public class VideoController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private VideoService videoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private MessageQueueService messageQueueService;

    @Autowired
    private IdService idService;

//    @Autowired
//    private UserClient userClient;
//
//    @Autowired
//    private MusicClient musicClient;

    @Resource
    private VideoOneService videoOneService;

    @Autowired
    private VideoOneDao VideoOneDao;
    @Autowired
    private FileStorageService fileStorageService;

    @ApiOperation(value = "上传视频的接口", notes = "将视频上传到文件服务器")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " file,", value = "文件", required = true, dataType = "MultipartFile", paramType = "query"),
            @ApiImplicitParam(name = " params", value = "map类存了哪些", required = true, dataType = "Map", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 404, message = "用户不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/upvideo")
    public ResponseEntity<?> upvideo(
            @ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file,
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
            video.setVideoComment(videoContext);
            video.setVideoUrl(videoUrl);
            video.setAuthId(Integer.parseInt(authId));
            video.setMusicId(Integer.parseInt(musicId));
            video.setVideoTitle(videoTitle);
            InputStream in= file.getInputStream();
            fileStorageService.uploadImgFile(videoId,videoId,in);
            // 5. 保存到数据库
            videoService.save(video);
            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            e.printStackTrace(); // 控制台输出异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器错误：" + e.getMessage());
        }
    }


    @GetMapping("/testbefore")
    public List<Map> getVideoInfobefore(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam String videoType
            ) {

        List<Map> one = videoOneService.getVideoInfo(page, size,videoType);

        return one;
    }

    @Resource
    private UserService userService;

    @Resource
    private MusicService musicService;

    @Resource
    private LikeoneService likeoneService;

    @Resource
    private CommentService commentService;
    @GetMapping("/test")
    public List<Map<String, Object>> getVideoInfo(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam String videoType
    ) {

        Page<Video> pageInfo = new Page<>(page, size);

        // 查询视频基本信息（含作者、音乐，按需要 join 查询或后处理）
        IPage<Video> videoPage = videoService.page(pageInfo,
                new QueryWrapper<Video>()
                        .eq("video_type", videoType) // 假设你有 video_type 字段
                        .orderByDesc("video_id")
        );

        List<Video> videos = videoPage.getRecords();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Video video : videos) {
            VideoVO vo = new VideoVO();
            BeanUtils.copyProperties(video, vo);

            // 用户信息
            User user = userService.getById(video.getAuthId());
            if (user != null) {
                vo.setUsername(user.getNickname());
                vo.setUserAvatar(user.getUserPic());
            }

            // 音乐信息
            Music music = musicService.getById(video.getMusicId());
            if (music != null) {
                vo.setMusic(music.getMusicName());
                vo.setMusicAvatar(music.getMusicAvatar());
                vo.setMusicId(music.getId());
            }

            // 点赞数 (count 返回 int，转为 Long)
            int likeCount = likeoneService.count(
                    new QueryWrapper<Likeone>().eq("like_videoid", video.getVideoId()).eq("`like`", 1)
            );
            vo.setLikeNum((long) likeCount);
            vo.setUserId((long) user.getId());

            // 评论数
            int commentCount = commentService.count(
                    new QueryWrapper<Comment>().eq("comment_video_id", video.getVideoId()).isNotNull("comment")
            );
            vo.setCommentNum((long) commentCount);

            // 收藏数
            int collectCount = likeoneService.count(
                    new QueryWrapper<Likeone>().eq("like_videoid", video.getVideoId()).eq("collect", 1)
            );
            vo.setCollectNum((long) collectCount);

            // 分享数
            int shareCount = likeoneService.count(
                    new QueryWrapper<Likeone>().eq("like_videoid", video.getVideoId()).eq("share", 1)
            );
            vo.setShareNum((long) shareCount);

            // 把 VideoVO 转 Map 返回
            Map<String, Object> map = new HashMap<>();
            map.put("videoId", vo.getVideoId());
            map.put("userId", vo.getUserId());
            map.put("videoUrl", vo.getVideoUrl());
            map.put("videoComment", vo.getVideoComment());
            map.put("username", vo.getUsername());
            map.put("userAvatar", vo.getUserAvatar());
            map.put("music", vo.getMusic());
            map.put("musicAvatar", vo.getMusicAvatar());
            map.put("likeNum", vo.getLikeNum());
            map.put("commentNum", vo.getCommentNum());
            map.put("collectNum", vo.getCollectNum());
            map.put("shareNum", vo.getShareNum());
            map.put("musicId", vo.getMusicId());

            result.add(map);
        }

        return result;
    }



    @GetMapping("/authbefore")
    public Map authbefore(

            int userid) {

        Map result = new HashMap();
        List<Map> one = VideoOneDao.getauth(userid);
        Map auth = one.get(0);
        List<Map> videobox = new ArrayList<Map>();
        for (Map two : one) {
            Map three = new HashMap();
            three.put("videoimgurl", String.valueOf(two.get("videoimgurl")));
            three.put("videohref", String.valueOf(two.get("videohref")));
            videobox.add(three);
        }

        result.put("videobox", videobox);
        result.put("auth", auth);
        return result;
    }

    @GetMapping("/auth")
    public Map<String, Object> getAuth(
            @RequestParam(required = false) Integer userid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Object UserIdObj = StpUtil.getLoginId();
        Long loginUserId = UserIdObj != null ? Long.parseLong(UserIdObj.toString()) : 0L;

        if (userid == null) {
            userid = loginUserId.intValue();  // 使用当前登录用户ID
        }

        Map<String, Object> result = new HashMap<>();

        // 查询用户信息
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getId, userid);
        User userInfo = userService.getOne(userQuery);
        result.put("auth", userInfo);

        // 查询视频分页数据
        Page<Video> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getAuthId, userid);
        IPage<Video> videoPage = videoService.page(pageInfo, videoQuery);

        result.put("videobox", videoPage.getRecords());
        result.put("total", videoPage.getTotal());

        return result;
    }


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping("/like")
    public ResponseEntity<String> like(@RequestBody Map<String, Object> requestone) {
        Map<String, Object> request= (Map<String, Object>) requestone.get("params");
        try {
            // 检查用户是否已登录
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            // 捕获未登录异常并返回 false
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户未登录");
        }

        Object userIdOb = StpUtil.getLoginId();
        Long userId = null;

        if (userIdOb != null) {
            // 先将 userIdOb 转换为 String，再转换为 Long
            userId = Long.valueOf(userIdOb.toString());
        }

        // 从 Map 中获取内容 ID 和 type（表示红心的颜色）
        Long contentId = Long.valueOf(request.get("contentId").toString());
        String type = (String) request.get("type");  // type 值判断
        String content_type = (String) request.get("content_type");  // type 值判断



        // 生成 Redis 键，通常以内容 ID 和用户 ID 组成
        String redisKey = "like:content:" +content_type +contentId;

        // 如果 type 是 "red"，表示已点赞，进行取消点赞操作
        if ("red".equals(type)) {
            // 判断用户是否已经点赞（可以存储用户已点赞的状态）
            if (!redisTemplate.opsForSet().isMember(redisKey, userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您尚未点赞");
            }

            // 将用户 ID 从 Redis 中移除，表示取消点赞
            redisTemplate.opsForSet().remove(redisKey, userId);

            // 将点赞数减 1
            redisTemplate.opsForValue().decrement(redisKey + ":count", 1);

            // 构建消息对象（可以是 DTO）
            LikeMessage message = new LikeMessage(contentId, userId,"del", System.currentTimeMillis());

            // 发送到 MQ
            rabbitTemplate.convertAndSend("like.exchange", "like.video", message);


            return ResponseEntity.ok(content_type+"取消点赞成功");
        } else {
            // 如果 type 不是 "red"，表示未点赞，进行点赞操作

            // 判断用户是否已经点赞
            if (redisTemplate.opsForSet().isMember(redisKey, userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您已点赞");
            }

            // 将用户 ID 存入 Redis，表示该用户已点赞
            redisTemplate.opsForSet().add(redisKey, userId);

            // 将点赞数加 1
            redisTemplate.opsForValue().increment(redisKey + ":count", 1);


            // 构建消息对象（可以是 DTO）
            LikeMessage message = new LikeMessage(contentId, userId,"add", System.currentTimeMillis());

            // 发送到 MQ
            rabbitTemplate.convertAndSend("like.exchange", "like.video", message);


            return ResponseEntity.ok(content_type+"点赞成功");
        }
    }

    @GetMapping("/likeData")
    public ResponseEntity<Map<String, Object>> getLikeData(@RequestParam Long contentId) {
        try {
            // 检查用户是否已登录
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            // 如果用户未登录，返回未登录状态
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Object userIdOb = StpUtil.getLoginId();
        Long userId = (Long) userIdOb;

        // 生成 Redis 键，获取点赞数
        String redisKey = "like:content:" + contentId;
        Object likeCountOb = redisTemplate.opsForValue().get(redisKey + ":count");
            Long likeCount=(Long) likeCountOb;
        // 检查用户是否已点赞
        boolean hasLiked = redisTemplate.opsForSet().isMember(redisKey, userId);

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount != null ? likeCount : 0);  // 点赞数量
        response.put("hasLiked", hasLiked);  // 用户是否已点赞

        // 返回点赞数据
        return ResponseEntity.ok(response);
    }


//
//
//
//
//    @GetMapping("/one")
//    public R getone() {
//        List<Video> videos = this.videoService.list();
//        System.out.println("one");
//
//        List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
//        for (Video video : videos) {
//            VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
//            // 复制 Video 对象的属性到 VideoWithAvatar 对象
//            BeanUtils.copyProperties(video, videoWithAvatar);
//            Map<String, String> map = ThreadLocalUtil.get();
////todo   远程调用用户和音乐信息  后面异常要加到全局异常处理器   这里的异常需要更细节的处理  下面more接口还有一个异常
//            try {
//                String avatar = userClient.avatar(video.getAuthId(), map.get("Authorization")).getUserPic();
//                videoWithAvatar.setAvatar(avatar); // 这是第60行
//
//                String nickname = userClient.avatar(video.getAuthId(), map.get("Authorization")).getNickname();
//                videoWithAvatar.setNickname(nickname); // 这是第60行
//
//                String musicAvatar = musicClient.avatar(video.getMusicId());
//                videoWithAvatar.setMusicAvatar(musicAvatar); //
//                videosWithAvatar.add(videoWithAvatar);
//
//            } catch (Exception e) {
//                System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /one接口");
//
//            }
//        }
//        return success(videosWithAvatar);
//    }

//    @GetMapping("/more")
//    public R getmore() {
//        List<Video> videos = this.videoService.list();
//        System.out.println("more");
//
//        List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
//        for (Video video : videos) {
//            VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
//            // 复制 Video 对象的属性到 VideoWithAvatar 对象
//            BeanUtils.copyProperties(video, videoWithAvatar);
//            Map<String, String> map = ThreadLocalUtil.get();
//            try {
//                String avatar = userClient.avatar(video.getAuthId(), map.get("Authorization")).getUserPic();
//                videoWithAvatar.setAvatar(avatar); // 这是第60行
//
//                String nickname = userClient.avatar(video.getAuthId(), map.get("Authorization")).getNickname();
//                videoWithAvatar.setNickname(nickname); // 这是第60行
//
//                String musicAvatar = musicClient.avatar(video.getMusicId());
//                videoWithAvatar.setMusicAvatar(musicAvatar); //
//                videosWithAvatar.add(videoWithAvatar);
//
//            } catch (Exception e) {
//                System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /more接口");
//
//            }
//
//
//        }
//        return success(videosWithAvatar);
//    }


}

