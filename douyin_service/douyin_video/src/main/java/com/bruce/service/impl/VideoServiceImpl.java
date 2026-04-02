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
import com.bruce.dto.LikeMsg;
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
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private RedisTemplate redisTemplate;


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
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送视频点赞消息（对象格式）
     */
    public void sendLikeMessage(LikeMsg msg) {
        // 路由 key 使用 videoId，保证同一个视频永远路由到同一个队列
        String routingKey = msg.getVideoId().toString();

        // 发送对象消息（Spring 会自动序列化成 JSON）
        rabbitTemplate.convertAndSend(
                "video.like.exchange",
                routingKey,
                msg
        );
    }

    @Override
    public Result<Map<String, Object>> handleLike(LikeRequestDTo likeRequest) {

        //todo  所谓幂等性 其实就是找一个全局唯一的id 然后在业务代码做校验  比如这里的判断rediskey是否在redis存在  存在的话直接return
        //todo  可以有多层操作  首先判断redis  然后再判断数据库 如果插入失败根据唯一id  直接return等  业务代码就是if判断是否存在或者处理过
        //todo  可以在这里根据唯一id判断是否操作过 如果操作过直接return
//---------------------------------------------------------------------------------------------------------
        //todo 这里可以看看  接口幂等性如何实现 还有常见的需要实现接口幂等性的业务
        //todo 这里可以看看  接口幂等性如何实现 还有常见的需要实现接口幂等性的业务
        //todo 这里可以看看  接口幂等性如何实现 还有常见的需要实现接口幂等性的业务
        //todo 这里可以看看  接口幂等性如何实现 还有常见的需要实现接口幂等性的业务
        //todo 示例 1：点赞接口（唯一 ID = userId + videoId）
        //todo 示例 2：提交订单接口（唯一 ID = orderToken /requestId）
        //todo 示例 3：支付回调接口（唯一 ID = outTradeNo 商户订单号）
// 1. 幂等判断：是否已点赞
//        Boolean isMember = redis.opsForSet().isMember(key, videoId.toString());
//        if (Boolean.TRUE.equals(isMember)) {
//            return Result.fail("已点赞");
//        }
//
//        // 2. DB 原子插入（唯一索引兜底）
//        int rows = likeMapper.insertIgnore(userId, videoId);
//        if (rows == 0) {
//            return Result.fail("已点赞");
//        }
//---------------------------------------------------------------------------------------------------------


        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        Long contentId = likeRequest.getContentId();
        String type = likeRequest.getType();
        String contentType = likeRequest.getContentType();
        String redisKey = "like:content:" + contentType + contentId;
        boolean isLiked = redisTemplate.opsForSet().isMember(redisKey, userId);
        LikeMsg msg=new LikeMsg();
        msg.setVideoId(contentId);
        msg.setUserId(userId);
        msg.setDelta(+1);
        msg.setTimestamp(System.currentTimeMillis() / 1000);
        if ("red".equals(type)) {
            // 点赞操作
            if (!isLiked) {
                msg.setDelta(+1);
            } else {
                return Result.success(new HashMap<>());
            }
        } else {
            // 取消点赞操作
            if (isLiked) {
                msg.setDelta(-1);
            } else {
                return Result.success(new HashMap<>());
            }
        }
        //todo MQ异步
        //todo MQ异步
        //todo MQ异步
        System.out.println("这里把消息发送到了MQ 让MQ实现异步落库");
        System.out.println("这里把消息发送到了MQ 让MQ实现异步落库");


        sendLikeMessage(msg);
        //todo  这里消费者消费消息  可以开启多线程快速落库 可以涉及到多线程的知识
        //todo  这里消费者消费消息  可以开启多线程快速落库 可以涉及到多线程的知识
        //todo  这里消费者消费消息  可以开启多线程快速落库 可以涉及到多线程的知识

//---------------------------------------------------------------------------------------------------------
        //todo  异步落库设计到两个表  一个是video的技术 like_count  另一个就是点赞记录表 点赞就插入 取消点赞把状态设置成0
        //todo  其中点赞技术策略有一个点需要注意  并发数太大 不能直接  update video set like_count=like_count+1 容易造成锁冲突 & 性能崩溃。
        //todo  ✔ 推荐使用 Redis 原子计数器 INCR video_like_count:video_id
        //todo  然后定时（或通过 MQ）： UPDATE video SET like_count = redis_value

//---------------------------------------------------------------------------------------------------------
        //todo  如果采用上面的计数方式  又会导致一个新的问题 就是redis的计数  和video表的计数  还有video_like聚合计数不一致
        //todo  允许 video.like_count ≠ video_like 聚合值   因为两个表的用途完全不同
        //todo  但是redis里的计数值和video的计数值是需要最终一致的 但可以短暂不一致
        //todo  最终一致  这里就涉及到如何实现最终一致性
        //todo  最终一致  这里就涉及到如何实现最终一致性
        //todo  最终一致  这里就涉及到如何实现最终一致性
        //todo  最终一致  这里就涉及到如何实现最终一致性
        //todo 机制 1：Redis 负责实时计数（INCR）
        //todo 机制 2：用 MQ 传递增量（delta）到 MySQL（异步）
        //todo 机制 3：定时（每秒/每分钟）同步一次全量值
        //todo 机制 4：每天凌晨执行“对账任务”



//---------------------------------------------------------------------------------------------------------


        //todo 下面的dolike只处理了redis里的操作  还有一个落库的操作 MQ异步
        //todo 为什么落库要用MQ而不用多线程 |线程数量有限，扛不住高并发 | 多线程无法削峰 | 多线程没有持久化，宕机全丢 | 多线程没有失败重试机制 | 多线程无法保证顺序写入
        //todo 为什么落库要用MQ而不用多线程 |线程数量有限，扛不住高并发 | 多线程无法削峰 | 多线程没有持久化，宕机全丢 | 多线程没有失败重试机制 | 多线程无法保证顺序写入
        //todo 为什么落库要用MQ而不用多线程 |线程数量有限，扛不住高并发 | 多线程无法削峰 | 多线程没有持久化，宕机全丢 | 多线程没有失败重试机制 | 多线程无法保证顺序写入
        return doLike(likeRequest);

    }


    // === 下面的业务方法不变 ===
    private Result<Map<String, Object>> doLike(LikeRequestDTo likeRequest) {
        //todo  所谓幂等性 其实就是找一个全局唯一的id 然后在业务代码做校验  比如这里的判断rediskey是否在redis存在  存在的话直接return
        //todo  所谓幂等性 其实就是找一个全局唯一的id 然后在业务代码做校验  比如这里的判断rediskey是否在redis存在  存在的话直接return
        //todo  可以有多层操作  首先判断redis  然后再判断数据库 如果插入失败根据唯一id  直接return等  业务代码就是if判断是否存在或者处理过
        //todo  可以有多层操作  首先判断redis  然后再判断数据库 如果插入失败根据唯一id  直接return等  业务代码就是if判断是否存在或者处理过
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        Long contentId = likeRequest.getContentId();
        String type = likeRequest.getType();
        String contentType = likeRequest.getContentType();
        String redisKey = "like:content:" + contentType + contentId;
        boolean isLiked = redisTemplate.opsForSet().isMember(redisKey, userId);
        String resultMessage;
        boolean success = false;
        if ("red".equals(type)) {
            // 点赞操作
            if (!isLiked) {
                // 将用户 ID 存入 Redis，表示该用户已点赞
                redisTemplate.opsForSet().add(redisKey, userId);
                // 将点赞数加 1
                redisTemplate.opsForValue().increment(redisKey + ":count", 1);
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
                resultMessage = contentType + "取消点赞成功";
                success = true;
            } else {
                return Result.error("您尚未点赞");
            }
        }
        // 返回统一格式的响应
        Map<String, Object> data = new HashMap<>();
        data.put("isLiked", "red".equals(type) ? true : false);
        return Result.success(data);
    }

}
