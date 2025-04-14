package com.bruce.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dao.VideoOneDao;
import com.bruce.entity.Video;
import com.bruce.entity.VideoOne;
import com.bruce.entity.VideoWithAvatar;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.IdService;
import com.bruce.service.MessageQueueService;
import com.bruce.service.VideoOneService;
import com.bruce.service.VideoService;
//import com.bruce.MusicClient;
//import com.bruce.UserClient;
import com.bruce.service.impl.IdServiceImpl;
import com.bruce.utils.ThreadLocalUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
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
    private RedisTemplate<String, String> redisTemplate;

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


    @GetMapping("/test")
    public List<Map> getVideoInfo(
            @RequestParam(defaultValue = "1") int page,

            @RequestParam(defaultValue = "4") int size,
            @RequestParam String videoType
            ) {



//        if (videoType.equals("tuijian")) {
//            List<Map> one = videoOneService.getVideoInfo(page, size,videoType);
//
//        } else if (videoType.equals("tongcheng")) {
//            List<Map> one = videoOneService.getVideoInfo(page, size,videoType);
//
//        } else if (videoType.equals("guanzhu")) {
//            List<Map> one = videoOneService.getVideoInfo(page, size,videoType);
//
//        } else if (videoType.equals("zhibo"))
//        {
//            List<Map> one = videoOneService.getVideoInfo(page, size,videoType);
//
//        }
//


        List<Map> one = videoOneService.getVideoInfo(page, size,videoType);
        return one;
    }


    @GetMapping("/auth")
    public Map getauth(

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


    @PostMapping("/like")
    public int like(
            @RequestParam(defaultValue = "1") int userid,
            @RequestParam(defaultValue = "4") int videoid) {


        String redisKey = "like:content:" + videoid;

        // 1. 检查用户是否已点赞
        Boolean isMember = redisTemplate.opsForSet().isMember(redisKey, userid);
        if (Boolean.TRUE.equals(isMember)) {
            // 用户已点赞，不需要再处理
            throw new RuntimeException("User has already liked this content");
        }

        // 2. 用户未点赞，将用户 ID 加入 Redis 的 Set
        redisTemplate.opsForSet().add(redisKey, String.valueOf(userid));
        // 3. 异步推送点赞事件到消息队列，稍后更新数据库中的点赞数

        messageQueueService.sendMessage(videoid);

        return 1;
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

