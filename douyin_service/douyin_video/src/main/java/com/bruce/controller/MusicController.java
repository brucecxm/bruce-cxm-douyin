package com.bruce.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dao.musiconeDao;
import com.bruce.entity.Music;
import com.bruce.entity.Video;
import com.bruce.service.MusicService;
import com.bruce.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Music)表控制层
 *
 * @author makejava
 * @since 2024-06-25 18:10:38
 */
@RestController
@RequestMapping("/video/music")
public class MusicController extends ApiController {


    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);


    @Autowired
    private musiconeDao musiconedao;

    /**
     * 服务对象
     */
    @Resource
    private MusicService musicService;


    @Resource
    private VideoService videoService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.musicService.getById(id));
    }

    @GetMapping("/getmusicbefore")
    public Map selectOnebefore(String musicId) {
        List<Map> one = musiconedao.one(musicId);
        List<Map> videoArr = new ArrayList<Map>();
        for (Map a : one) {
            Map ss = new HashMap<>();
            ss.put("video_img", a.get("video_img"));
            ss.put("linkurl", a.get("linkurl"));
            videoArr.add(ss);
        }
        Map oneww = new HashMap();
        oneww.put("videoimg", videoArr);
        oneww.put("music_info", one.get(0));
        return oneww;

    }

    @GetMapping("/getmusic")
    public Map<String, Object> selectOne(
            @RequestParam(value = "musicId", required = true) String musicId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // 查询music信息
        Music music = musicService.getOne(new QueryWrapper<Music>().eq("id", musicId));
        if (music == null) {
            throw new RuntimeException("未找到该音乐信息");
        }

        // 分页查询视频
        Page<Video> videoPage = new Page<>(page, size);
        videoService.page(videoPage, new QueryWrapper<Video>().eq("music_id", music.getId()));

        // 封装视频数组
        List<Map<String, Object>> videoArr = new ArrayList<>();
        for (Video video : videoPage.getRecords()) {
            Map<String, Object> videoMap = new HashMap<>();
            videoMap.put("video_img", video.getCover());
            videoMap.put("link_url", video.getVideoId()); // 建议用实际视频ID
            videoArr.add(videoMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("videoimg", videoArr);
        result.put("music_info", music);
        result.put("total", videoPage.getTotal()); // 总条数
        result.put("page", page);
        result.put("size", size);

        return result;
    }




    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.musicService.removeByIds(idList));
    }
}

