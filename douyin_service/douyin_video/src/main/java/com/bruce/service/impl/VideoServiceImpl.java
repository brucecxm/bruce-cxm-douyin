package com.bruce.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.dict.Constant;
import com.bruce.dto.LikeMessage;
import com.bruce.dto.LikeRequestDTo;
import com.bruce.entity.*;
import com.bruce.file.service.FileStorageService;
import com.bruce.pojo.Result;
import com.bruce.service.*;
import com.bruce.service.strategy.VideoStrategy;
import com.bruce.service.strategy.VideoStrategyFactory;
import com.bruce.vo.VideoVO;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
@Service("videoService")
public class  VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private MessageQueueService messageQueueService;

    @Autowired
    private IdService idService;

    @Autowired
    private FileStorageService fileStorageService;

    @Resource
    private MusicService musicService;

    @Resource
    private LikeService likeService;

    @Resource
    private FavorService favorService;
    @Resource
    private CommentService commentService;

    @Resource
    private VideoStrategyFactory videoStrategyFactory;

    @Override
    public List<VideoVO> getVideoPage(int page,
            int size,
            String videoType) {
        // 根据videoType获取对应的策略
        VideoStrategy strategy = videoStrategyFactory.getStrategy(videoType);
        // 调用策略的getVideos方法获取视频列表
        return strategy.getVideos(page, size);
    }






    // 注入 Spring 管理的线程池
    @Autowired
    @Qualifier("likeThreadPool")
    private Executor likeThreadPool;

    // === 下面的业务方法不变 ===
    private  Result<Map<String, Object>> doLike(LikeRequestDTo likeRequest) {

        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());

        // 限流逻辑：每个用户每秒最多允许10次点赞操作
        String rateLimitKey = "rate:limit:like:" + userId;
        long current = System.currentTimeMillis() / 1000;
        redisTemplate.opsForHash().put(rateLimitKey, "last", current);
        redisTemplate.expire(rateLimitKey, 1, java.util.concurrent.TimeUnit.SECONDS);
        Long count = redisTemplate.opsForHash().increment(rateLimitKey, "count", 1);
        if (count > 10) {
            return Result.error("点赞操作过于频繁，请稍后再试");
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
                return Result.error("您已点赞");
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
                return Result.error("您尚未点赞");
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

        return Result.success(data);
    }

    private void updateLikeCount(Long videoId) {
        try { Thread.sleep(1000); } catch (Exception e) {}
        System.out.println("【异步线程】更新点赞数+1");
    }

    private void sendNotify(Long userId, Long videoId) {
        try { Thread.sleep(500); } catch (Exception e) {}
        System.out.println("【异步线程】发送点赞通知");
    }
    @Override
    public Result<Map<String, Object>> handleLike(LikeRequestDTo likeRequest) {
        System.out.println("【主线程】用户点赞开始");


        // 2. 非核心逻辑：交给线程池异步执行
        likeThreadPool.execute(() -> {
            updateLikeCount(1L); // 更新计数
            sendNotify(1L, 1L); // 发通知
        });

        // 3. 立刻返回！用户无等待
        System.out.println("【主线程】点赞成功，直接返回");

        return doLike(likeRequest);

    }

}
