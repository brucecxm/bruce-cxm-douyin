# 百万级点赞场景下的消息队列优化方案（基于项目实际配置）

## 一、项目当前RabbitMQ配置分析

### 1. 点赞相关配置

**交换机配置**（RabbitConfig.java:81-88）：
```java
public static final String EXCHANGE = "like.exchange";
public static final String QUEUE = "like.video.queue";
public static final String ROUTING_KEY = "like.video";

@Bean
public TopicExchange likeExchange() {
    return new TopicExchange(EXCHANGE); // 未设置持久化，默认false
}
```

**队列配置**（RabbitConfig.java:90-93）：
```java
@Bean
public Queue likeQueue() {
    return new Queue(QUEUE); // 未设置持久化，默认false
}
```

**绑定配置**（RabbitConfig.java:95-98）：
```java
@Bean
public Binding likeBinding() {
    return BindingBuilder.bind(likeQueue()).to(likeExchange()).with(ROUTING_KEY);
}
```

### 2. 存在的问题

1. **非持久化配置**：
   - 交换机和队列均未设置持久化
   - 消息默认也不会持久化
   - RabbitMQ重启后，交换机、队列和消息都会丢失

2. **队列类型问题**：
   - 使用默认队列类型，不适合存储大量消息
   - 缺乏懒加载配置，大量消息会占用过多内存

3. **缺乏死信队列**：
   - 消费失败的消息没有处理机制
   - 可能导致队列阻塞

4. **消息发送方式**：
   - 当前代码每条点赞操作都直接发送一条消息
   - 百万级点赞会产生百万条消息，效率低下

## 二、优化方案

### 1. 优化RabbitMQ配置

#### 1.1 配置持久化

**修改RabbitConfig.java**：
```java
@Bean
public TopicExchange likeExchange() {
    // 设置交换机持久化
    return new TopicExchange(EXCHANGE, true, false);
}

@Bean
public Queue likeQueue() {
    // 设置队列持久化和懒加载
    return QueueBuilder.durable(QUEUE)
            .withArgument("x-queue-mode", "lazy") // 懒加载，适合大量消息存储
            .build();
}
```

#### 1.2 配置死信队列

**修改RabbitConfig.java**：
```java
// 死信队列配置
public static final String DEAD_LETTER_EXCHANGE = "like.dead.letter.exchange";
public static final String DEAD_LETTER_QUEUE = "like.dead.letter.queue";
public static final String DEAD_LETTER_ROUTING_KEY = "like.dead.letter";

// 声明死信交换机
@Bean
public DirectExchange deadLetterExchange() {
    return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
}

// 声明死信队列
@Bean
public Queue deadLetterQueue() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
}

// 绑定死信队列
@Bean
public Binding deadLetterBinding() {
    return BindingBuilder.bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with(DEAD_LETTER_ROUTING_KEY);
}

// 修改点赞队列，添加死信配置
@Bean
public Queue likeQueue() {
    return QueueBuilder.durable(QUEUE)
            .withArgument("x-queue-mode", "lazy")
            // 配置死信交换机
            .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
            // 配置死信路由键
            .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
            // 配置消息过期时间（可选）
            .withArgument("x-message-ttl", 3600000) // 1小时后过期
            .build();
}
```

### 2. 优化点赞接口实现

#### 2.1 实现批量消息发送

**修改VideoController.java中的like方法**：

```java
@PostMapping("/like")
public ResponseEntity<String> like(@RequestBody Map<String, Object> requestone) {
    Map<String, Object> request = (Map<String, Object>) requestone.get("params");
    try {
        StpUtil.checkLogin();
    } catch (NotLoginException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户未登录");
    }
    
    // 原有参数处理逻辑...
    
    // 生成Redis键
    String redisKey = "like:content:" + content_type + contentId;
    String batchKey = "like:batch:" + content_type;
    
    // 点赞/取消点赞逻辑...
    
    // 优化：将点赞消息先存入Redis列表，而非直接发送到MQ
    LikeMessage message = new LikeMessage(contentId, userId, operation, System.currentTimeMillis());
    redisTemplate.opsForList().rightPush(batchKey, message);
    
    // 检查是否达到批量发送阈值
    Long size = redisTemplate.opsForList().size(batchKey);
    if (size >= 100) { // 每100条消息批量发送一次
        // 批量发送消息到MQ
        List<LikeMessage> messages = redisTemplate.opsForList().range(batchKey, 0, 99);
        if (messages != null && !messages.isEmpty()) {
            // 设置消息持久化
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, messages, message -> {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            });
            // 删除已发送的消息
            redisTemplate.opsForList().trim(batchKey, 100, -1);
        }
    }
    
    return ResponseEntity.ok(content_type + ("red".equals(type) ? "取消点赞成功" : "点赞成功"));
}
```

#### 2.2 添加定时任务，确保消息及时处理

**创建LikeBatchTask.java**：

```java
package com.bruce.task;

import com.bruce.entity.LikeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.bruce.config.RabbitConfig.EXCHANGE;
import static com.bruce.config.RabbitConfig.ROUTING_KEY;

@Component
public class LikeBatchTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定时批量发送点赞消息到MQ
     * 每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void batchSendLikeMessages() {
        // 获取所有批量点赞key
        Set<String> batchKeys = redisTemplate.keys("like:batch:*");
        if (batchKeys == null || batchKeys.isEmpty()) {
            return;
        }

        for (String batchKey : batchKeys) {
            // 获取当前key下的所有消息
            List<LikeMessage> messages = redisTemplate.opsForList().range(batchKey, 0, -1);
            if (messages != null && !messages.isEmpty()) {
                // 批量发送消息到MQ，并设置持久化
                rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, messages, message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });
                // 清空Redis列表
                redisTemplate.delete(batchKey);
            }
        }
    }
}
```

### 3. 优化消费端实现

#### 3.1 增加消费者数量

**修改消费者监听配置**：

```java
@RabbitListener(
    queues = RabbitConfig.QUEUE,
    concurrency = "10-50" // 设置并发消费者数量，范围10-50
)
public void handleLikeMessage(List<LikeMessage> messages) {
    // 批量处理点赞消息
    for (LikeMessage message : messages) {
        // 处理点赞逻辑...
    }
}
```

#### 3.2 实现批量数据库操作

**优化数据库更新逻辑**：

```java
@RabbitListener(
    queues = RabbitConfig.QUEUE,
    concurrency = "10-50"
)
public void handleLikeMessage(List<LikeMessage> messages) {
    // 按内容ID分组
    Map<Long, List<LikeMessage>> messagesByContentId = messages.stream()
            .collect(Collectors.groupingBy(LikeMessage::getContentId));
    
    // 批量处理每组消息
    for (Map.Entry<Long, List<LikeMessage>> entry : messagesByContentId.entrySet()) {
        Long contentId = entry.getKey();
        List<LikeMessage> contentMessages = entry.getValue();
        
        // 统计点赞和取消点赞数量
        long addCount = contentMessages.stream()
                .filter(msg -> "add".equals(msg.getOperation()))
                .count();
        long delCount = contentMessages.stream()
                .filter(msg -> "del".equals(msg.getOperation()))
                .count();
        
        // 批量更新数据库
        if (addCount > 0 || delCount > 0) {
            // 批量更新点赞数量
            likeService.batchUpdateLikeCount(contentId, addCount - delCount);
            
            // 批量保存点赞记录
            List<Like> likeList = contentMessages.stream()
                    .map(msg -> {
                        Like like = new Like();
                        like.setContentId(msg.getContentId());
                        like.setUserId(msg.getUserId());
                        like.setOperation(msg.getOperation());
                        like.setCreateTime(new Date(msg.getTimestamp()));
                        return like;
                    })
                    .collect(Collectors.toList());
            likeService.saveBatch(likeList);
        }
    }
}
```

### 4. 添加限流机制

#### 4.1 实现Redis限流

**修改VideoController.java中的like方法**：

```java
@PostMapping("/like")
public ResponseEntity<String> like(@RequestBody Map<String, Object> requestone) {
    // 限流逻辑：每个用户每秒最多允许10次点赞操作
    String userIdStr = String.valueOf(StpUtil.getLoginId());
    String rateLimitKey = "rate:limit:like:" + userIdStr;
    
    // 使用Redis实现令牌桶限流
    long current = System.currentTimeMillis() / 1000;
    redisTemplate.opsForHash().put(rateLimitKey, "last", current);
    redisTemplate.expire(rateLimitKey, 1, TimeUnit.SECONDS);
    
    Long count = redisTemplate.opsForHash().increment(rateLimitKey, "count", 1);
    if (count > 10) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("点赞操作过于频繁，请稍后再试");
    }
    
    // 原有点赞逻辑...
}
```

## 三、监控与告警

### 1. 关键监控指标

| 指标名称 | 监控对象 | 告警阈值 |
|---------|---------|---------|
| 队列长度 | like.video.queue | > 10000 |
| 消费延迟 | like.video.queue | > 60秒 |
| 消息确认率 | like.video.queue | < 99% |
| 内存使用率 | RabbitMQ服务器 | > 80% |
| 磁盘使用率 | RabbitMQ服务器 | > 70% |

### 2. 监控实现

- **使用Prometheus + Grafana**：监控RabbitMQ各项指标
- **配置告警规则**：当指标超过阈值时，通过邮件、短信等方式告警
- **定期检查队列状态**：使用RabbitMQ Management API定期检查队列状态

## 四、优化效果预期

| 优化项 | 预期效果 |
|-------|---------|
| 批量消息发送 | 减少99%的MQ消息数量 |
| 持久化配置 | 确保消息不丢失 |
| 懒加载队列 | 降低内存使用率，提高系统稳定性 |
| 死信队列 | 避免队列阻塞，提高系统可靠性 |
| 批量消费 | 提高消费效率，减少数据库压力 |
| 限流机制 | 防止恶意请求，保护系统安全 |

## 五、注意事项

1. **RabbitMQ插件依赖**：
   - 项目中已使用延迟交换机，需确保`rabbitmq_delayed_message_exchange`插件已安装
   - 懒加载队列无需额外插件

2. **Redis配置**：
   - 确保Redis服务稳定，避免单点故障
   - 考虑使用Redis集群，提高可用性

3. **数据库优化**：
   - 批量更新时，确保数据库支持高效的批量操作
   - 考虑添加适当的索引，提高查询效率

4. **测试建议**：
   - 在测试环境进行压力测试，验证优化效果
   - 逐步增加并发量，观察系统性能变化
   - 测试故障恢复情况，确保数据一致性

通过以上优化方案，可以有效解决百万级点赞场景下消息队列可能出现的各种异常情况，提高系统的稳定性和性能。