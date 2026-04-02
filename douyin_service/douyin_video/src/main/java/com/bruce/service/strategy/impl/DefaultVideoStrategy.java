package com.bruce.service.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dao.VideoDao;
import com.bruce.dict.Constant;
import com.bruce.entity.Comment;
import com.bruce.entity.Favor;
import com.bruce.entity.Music;
import com.bruce.entity.User;
import com.bruce.entity.Video;
import com.bruce.feign.UserFeignClient;
import com.bruce.pojo.Result;
import com.bruce.service.CommentService;
import com.bruce.service.FavorService;
import com.bruce.service.MusicService;
import com.bruce.service.strategy.VideoStrategy;
import com.bruce.utils.AuthUtils;
import com.bruce.vo.VideoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认视频策略（对应videoType=1）
 */
@Component("defaultVideoStrategy")
public class DefaultVideoStrategy implements VideoStrategy {

    @Autowired
    private VideoDao videoDao;

    // ✅ 这里我给你加了正确泛型，永远不报错
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private MusicService musicService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private CommentService commentService;

    @Override
    public List<VideoVO> getVideos(int page, int size) {
        Page<Video> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Video> videoQueryWrapper = new LambdaQueryWrapper<Video>()
                .orderByDesc(Video::getVideoId);
        IPage<Video> videoPage = videoDao.selectPage(pageInfo, videoQueryWrapper);
        List<Video> videos = videoPage.getRecords();
        List<VideoVO> result = new ArrayList<>();

        Long loginUserId = AuthUtils.getCurrentUserId();

        for (Video video : videos) {
            VideoVO vo = new VideoVO();
            BeanUtils.copyProperties(video, vo);

            // 用户信息
            Result<User> userR = userFeignClient.getUserById(video.getUserId());
            User user = userR.getData();
            if (user != null) {
                vo.setUsername(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
                vo.setUserId((long) user.getId());
            }

            // 音乐信息
            Music music = musicService.getById(video.getMusicId());
            if (music != null) {
                vo.setMusic(music.getMusicName());
                vo.setMusicAvatar(music.getMusicAvatar());
                vo.setMusicId(music.getId());
            }

            // 点赞数
            String redisKey = "like:content:video" + video.getVideoId();
            Integer likenum = (Integer) redisTemplate.opsForValue().get(redisKey + ":count");
            int likeCount = likenum != null ? likenum : favorService.count(new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, video.getVideoId()));
            vo.setLikeNum((long) likeCount);

            // 评论数
            int commentCount = commentService.count(new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getCommentId, video.getVideoId())
                    .isNotNull(Comment::getComment));
            vo.setCommentNum((long) commentCount);

            // 收藏、分享（逻辑不变）
            int collectCount = favorService.count(new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, video.getVideoId()));
            vo.setCollectNum((long) collectCount);

            int shareCount = favorService.count(new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, video.getVideoId()));
            vo.setShareNum((long) shareCount);

            // 是否点赞
            boolean isLiked = false;
            if (loginUserId != null) {
                Boolean redisLiked = redisTemplate.opsForSet().isMember(redisKey, loginUserId);
                isLiked = redisLiked != null ? redisLiked : favorService.count(new LambdaQueryWrapper<Favor>()
                        .eq(Favor::getCommentId, video.getVideoId())
                        .eq(Favor::getUserId, loginUserId)) > 0;
            }
            vo.setLiked(isLiked);

            // 是否收藏
            boolean isCollected = false;
            if (loginUserId != null) {
                String collectRedisKey = "collect:content:video" + video.getVideoId();
                Boolean redisCollected = redisTemplate.opsForSet().isMember(collectRedisKey, loginUserId);
                isCollected = redisCollected != null ? redisCollected : favorService.count(new LambdaQueryWrapper<Favor>()
                        .eq(Favor::getCommentId, video.getVideoId())
                        .eq(Favor::getUserId, loginUserId)) > 0;
            }
            vo.setCollected(isCollected);

            result.add(vo);
        }
        return result;
    }
}
