package com.bruce.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.service.VideoOneService;
import com.bruce.video.entity.Video;
import com.bruce.video.entity.VideoOne;
import com.bruce.video.entity.VideoWithAvatar;
import com.bruce.service.VideoService;
import com.bruce.music.MusicClient;
import com.bruce.user.UserClient;
import com.bruce.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (Video)表控制层
 *
 * @author makejava
 * @since 2024-06-25 13:03:23
 */
@RestController
@RequestMapping("/video")
public class VideoController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private VideoService videoService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MusicClient musicClient;

    @Resource
    private VideoOneService videoOneService;




    @PostMapping("/upvideo")
    public void upvideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("params")    Map params) {

        String videoTitle= String.valueOf(params.get("videoTitle"));
        String videoContext= String.valueOf(params.get("videoContext"));
        String authId="100112";
        String VideoId="100112";
        String MusicId="100112";
        String fileName=file.getName();
        StringBuilder videoUrl= new StringBuilder("http://192.168.200.130:9000/mybucket/");
         videoUrl.append(fileName);
       Video video=new Video();
       video.setVideoId(Integer.valueOf(VideoId));
       video.setVideoComment(videoContext);
       video.setVideoUrl(videoUrl.toString());
       video.setAuthId(Integer.valueOf(authId));
       video.setMusicId(Integer.valueOf(MusicId));
       video.setVideoTitle(videoTitle);
       this.videoService.save(video);
    }


    @GetMapping("/{videoId}")
    public List<VideoOne> getVideoInfo(
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "4") int size) {
        return videoOneService.getVideoInfo(page, size);
    }





    @GetMapping("/one")
    public R getone() {
        List<Video> videos = this.videoService.list();
        System.out.println("one");

        List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
        for (Video video : videos) {
            VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
            // 复制 Video 对象的属性到 VideoWithAvatar 对象
            BeanUtils.copyProperties(video, videoWithAvatar);
            Map<String, String> map = ThreadLocalUtil.get();
//todo   远程调用用户和音乐信息  后面异常要加到全局异常处理器   这里的异常需要更细节的处理  下面more接口还有一个异常
            try {
                String avatar = userClient.avatar(video.getAuthId(), map.get("Authorization")).getUserPic();
                videoWithAvatar.setAvatar(avatar); // 这是第60行

                String nickname = userClient.avatar(video.getAuthId(), map.get("Authorization")).getNickname();
                videoWithAvatar.setNickname(nickname); // 这是第60行

                String musicAvatar = musicClient.avatar(video.getMusicId());
                videoWithAvatar.setMusicAvatar(musicAvatar); //
                videosWithAvatar.add(videoWithAvatar);

            } catch (Exception e) {
                System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /one接口");

            }
        }
        return success(videosWithAvatar);
    }

    @GetMapping("/more")
    public R getmore() {
        List<Video> videos = this.videoService.list();
        System.out.println("more");

        List<VideoWithAvatar> videosWithAvatar = new ArrayList<>();
        for (Video video : videos) {
            VideoWithAvatar videoWithAvatar = new VideoWithAvatar();
            // 复制 Video 对象的属性到 VideoWithAvatar 对象
            BeanUtils.copyProperties(video, videoWithAvatar);
            Map<String, String> map = ThreadLocalUtil.get();
            try {
                String avatar = userClient.avatar(video.getAuthId(), map.get("Authorization")).getUserPic();
                videoWithAvatar.setAvatar(avatar); // 这是第60行

                String nickname = userClient.avatar(video.getAuthId(), map.get("Authorization")).getNickname();
                videoWithAvatar.setNickname(nickname); // 这是第60行

                String musicAvatar = musicClient.avatar(video.getMusicId());
                videoWithAvatar.setMusicAvatar(musicAvatar); //
                videosWithAvatar.add(videoWithAvatar);

            } catch (Exception e) {
                System.out.println("没有捕获到完整的视频信息 位于DouyinVideoService /more接口");

            }


        }
        return success(videosWithAvatar);
    }






}

