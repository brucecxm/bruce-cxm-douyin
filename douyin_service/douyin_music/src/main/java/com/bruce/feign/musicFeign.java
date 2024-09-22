package com.bruce.feign;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.bruce.music.entity.Music;
import com.bruce.service.MusicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2024-06-25 13:57:40
 */
@RestController
@RequestMapping("/musicfeign")
public class musicFeign extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MusicService musicService;

    @GetMapping("/avatar")
public String avatar(@RequestParam int musicId) {
    LambdaQueryWrapper<Music> queryWrapper=new LambdaQueryWrapper();
    queryWrapper.eq(Music::getMusicVideoId, musicId);
    System.out.println(musicId);
    System.out.println("comming");
  Music music=  this.musicService.getOne(queryWrapper);
  return music.getMusicAvatar();
}

}

