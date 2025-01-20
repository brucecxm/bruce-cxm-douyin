package com.bruce.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dao.VideoOneDao;
import com.bruce.entity.Video;
import com.bruce.entity.VideoOne;
import com.bruce.entity.VideoWithAvatar;
import com.bruce.service.VideoOneService;
import com.bruce.service.VideoService;
//import com.bruce.MusicClient;
//import com.bruce.UserClient;
import com.bruce.utils.ThreadLocalUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

//    @Autowired
//    private UserClient userClient;
//
//    @Autowired
//    private MusicClient musicClient;

    @Resource
    private VideoOneService videoOneService;

    @Autowired
    private VideoOneDao VideoOneDao;


    @PostMapping("/upvideo")
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
    public void upvideo(
            @ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file,
            @RequestParam("params") Map params) {

        String videoTitle = String.valueOf(params.get("videoTitle"));
        String videoContext = String.valueOf(params.get("videoContext"));
        String authId = "100112";
        String VideoId = "100112";
        String MusicId = "100112";
        String fileName = file.getName();
        StringBuilder videoUrl = new StringBuilder("http://192.168.200.130:9000/mybucket/");
        videoUrl.append(fileName);
        Video video = new Video();
        video.setVideoId(Integer.valueOf(VideoId));
        video.setVideoComment(videoContext);
        video.setVideoUrl(videoUrl.toString());
        video.setAuthId(Integer.valueOf(authId));
        video.setMusicId(Integer.valueOf(MusicId));
        video.setVideoTitle(videoTitle);
        this.videoService.save(video);
    }


    @GetMapping("/test")
    public List<Map> getVideoInfo(
            @RequestParam(defaultValue = "1") int page,

            @RequestParam(defaultValue = "4") int size) {


        List<Map> one = videoOneService.getVideoInfo(page, size);
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

        List<VideoOne> one = VideoOneDao.getlikeexit(userid, videoid);
        int result = 0;

        List<Map> two = VideoOneDao.getlikeexitMap(userid, videoid);


        if (one.size() > 0) {
            if (two.size() <= 0) {
                result = VideoOneDao.toFirstlike(userid, videoid);

            } else {
                result = VideoOneDao.tolike(userid, videoid);

            }

        } else {
            result = VideoOneDao.toFirstlike(userid, videoid);

        }


        if (result > 0) {
            // 更新成功
            return 1;
        } else {
            // 更新失败
            return 0;
        }
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

