package com.bruce.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.config.RabbitConfig;
import com.bruce.dto.LikeMessage;
import com.bruce.entity.Likeone;
import com.bruce.service.LikeoneService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LikeConsumer {

    @Autowired
    private LikeoneService likeoneService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleLikeMessage(LikeMessage message) {
        Likeone entity = new Likeone();
        entity.setLikeUserid(message.getUserId());
        entity.setLikeVideoid(message.getVideoId());
        if(message.getType().equals("del"))
        {
            // 取消点赞，构造查询条件删除记录
            LambdaQueryWrapper<Likeone> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Likeone::getLikeUserid, message.getUserId())
                    .eq(Likeone::getLikeVideoid, message.getVideoId());
            likeoneService.remove(queryWrapper);
        }else {
            entity.setCreateTime(new Date(message.getTimestamp()));
            likeoneService.save(entity); // JPA方式

        }


    }
}
