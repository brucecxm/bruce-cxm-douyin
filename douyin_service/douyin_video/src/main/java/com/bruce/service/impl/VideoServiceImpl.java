package com.bruce.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.dict.Constant;
import com.bruce.entity.*;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.*;
import com.bruce.vo.VideoVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {


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
    @Override
    public List<Map<String, Object>> getVideoPage(int page,
                                                  int size,
                                                  String videoType) {
        Page<Video> pageInfo = new Page<>(page, size);
        // LambdaQueryWrapper 用于 Video 表查询
        LambdaQueryWrapper<Video> videoQueryWrapper = new LambdaQueryWrapper<Video>()
                .orderByDesc(Video::getVideoId);
        IPage<Video> videoPage = videoService.page(pageInfo, videoQueryWrapper);
        List<Video> videos = videoPage.getRecords();
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取当前登录用户ID
        Long loginUserId = null;
        try {
            StpUtil.checkLogin();
            Object userIdObj = StpUtil.getLoginId();
            if (userIdObj instanceof Long) {
                loginUserId = (Long) userIdObj;
            } else if (userIdObj instanceof Integer) {
                loginUserId = ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                loginUserId = Long.parseLong((String) userIdObj);
            }
        } catch (NotLoginException e) {
            // 用户未登录，loginUserId保持为null
        }

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
            String redisKey = "like:content:" + "video" + video.getVideoId();
            Integer likenum = (Integer) redisTemplate.opsForValue().get(redisKey + ":count");
            int likeCount;
            if (likenum != null) {
                likeCount = likenum;
            } else {
                // LambdaQueryWrapper 用于 Like 表查询点赞数
                LambdaQueryWrapper<Favor> likeQueryWrapper = new LambdaQueryWrapper<Favor>()
                        .eq(Favor::getCommentId, video.getVideoId());

                likeCount = favorService.count(likeQueryWrapper);
            }
            vo.setLikeNum((long) likeCount);
            vo.setUserId((long) user.getId());
            // 评论数
            LambdaQueryWrapper<Comment> commentQueryWrapper = new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getCommentId, video.getVideoId())
                    .isNotNull(Comment::getComment);
            int commentCount = commentService.count(commentQueryWrapper);
            vo.setCommentNum((long) commentCount);
            // 收藏数
            LambdaQueryWrapper<Favor> collectWrapper = new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, video.getVideoId());
            // .eq(Like::getCollect, 1);
            int collectCount = favorService.count(collectWrapper);
            vo.setCollectNum((long) collectCount);
            // 分享数
            LambdaQueryWrapper<Favor> shareWrapper = new LambdaQueryWrapper<Favor>()
                    .eq(Favor::getCommentId, video.getVideoId());
            int shareCount = favorService.count(shareWrapper);
            vo.setShareNum((long) shareCount);

            // 判断当前用户是否已点赞
            boolean isLiked = false;
            if (loginUserId != null) {
                // 先从Redis查询点赞状态
                Boolean redisLiked = redisTemplate.opsForSet().isMember(redisKey, loginUserId);
                if (redisLiked != null) {
                    isLiked = redisLiked;
                } else {
                    // Redis中没有，从数据库查询
                    LambdaQueryWrapper<Favor> userLikeQuery = new LambdaQueryWrapper<Favor>()
                            .eq(Favor::getCommentId, video.getVideoId())
                            .eq(Favor::getUserId, loginUserId);
                    isLiked = favorService.count(userLikeQuery) > 0;
                }
            }

            // 判断当前用户是否已收藏
            boolean isCollected = false;
            if (loginUserId != null) {
                // 先从Redis查询收藏状态
                String collectRedisKey = "collect:content:" + "video" + video.getVideoId();
                Boolean redisCollected = redisTemplate.opsForSet().isMember(collectRedisKey, loginUserId);
                if (redisCollected != null) {
                    isCollected = redisCollected;
                } else {
                    // Redis中没有，从数据库查询
                    LambdaQueryWrapper<Favor> userCollectQuery = new LambdaQueryWrapper<Favor>()
                            .eq(Favor::getCommentId, video.getVideoId())
                            .eq(Favor::getUserId, loginUserId);
                    isCollected = favorService.count(userCollectQuery) > 0;
                }
            }

            // 组装返回数据
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.ApiParam.VIDEO_ID, vo.getVideoId());
            map.put(Constant.ApiParam.USER_ID, vo.getUserId());
            map.put(Constant.ApiParam.CONTENT, vo.getContent());
            map.put(Constant.Video.VIDEO_URL, vo.getUrl());
            map.put(Constant.User.USER_NAME, vo.getUsername());
            map.put(Constant.User.USER_AVATAR, vo.getUserAvatar());
            map.put(Constant.Music.MUSIC, vo.getMusic());
            map.put(Constant.Music.MUSIC_AVATAR, vo.getMusicAvatar());
            map.put(Constant.Stat.LIKE_NUM, vo.getLikeNum());
            map.put(Constant.Stat.COMMENT_NUM, vo.getCommentNum());
            map.put(Constant.Stat.COLLECT_NUM, vo.getCollectNum());
            map.put(Constant.Stat.SHARE_NUM, vo.getShareNum());
            map.put(Constant.ApiParam.MUSIC_ID, vo.getMusicId());
            map.put("isLiked", isLiked); // 添加是否点赞的标识
            map.put("isCollected", isCollected); // 添加是否收藏的标识
            result.add(map);
        }
        return result;
    }

}

