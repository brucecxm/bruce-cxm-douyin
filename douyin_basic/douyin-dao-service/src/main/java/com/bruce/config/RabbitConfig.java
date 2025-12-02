package com.bruce.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("YanZhengQueue", true);
    }
    // 交换机名称（延迟交换机）
    public static final String NOTIFY_DELAY_EXCHANGE = "chat.notify.exchange";
    // 队列名称（接收延迟消息的队列）
    public static final String NOTIFY_DELAY_QUEUE = "chat.notify.delay.queue";
    // 路由键（绑定交换机和队列的规则）
    public static final String NOTIFY_DELAY_ROUTING_KEY = "chat.notify.delay";

    /**
     * 声明延迟交换机（x-delayed-message类型）
     * 需安装rabbitmq_delayed_message_exchange插件才能使用
     */
    @Bean
    public CustomExchange notifyDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        // 指定交换机类型为延迟交换机
        args.put("x-delayed-type", "direct"); // 延迟交换机内部可使用direct/topic等路由策略
        // 参数：交换机名称、类型、是否持久化、是否自动删除、附加参数
        return new CustomExchange(NOTIFY_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 声明延迟队列（用于接收延迟后的消息）
     */
    @Bean
    public Queue notifyDelayQueue() {
        // 参数：队列名称、是否持久化、是否排他、是否自动删除、附加参数
        return QueueBuilder.durable(NOTIFY_DELAY_QUEUE)
                .withArgument("x-queue-mode", "lazy") // 懒加载队列（优化大量消息存储）
                .build();
    }

    /**
     * 绑定交换机和队列（通过路由键）
     */
    @Bean
    public Binding notifyDelayBinding() {
        // 将队列通过路由键绑定到交换机
        return BindingBuilder.bind(notifyDelayQueue())
                .to(notifyDelayExchange())
                .with(NOTIFY_DELAY_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email.queue", true);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();  // 使用 JSON 序列化消息转换器
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // 点赞相关配置
    public static final String EXCHANGE = "like.exchange";
    public static final String QUEUE = "like.video.queue";
    public static final String ROUTING_KEY = "like.video";
    
    // 批量点赞相关配置
    public static final String BATCH_QUEUE = "like.video.batch.queue";
    public static final String BATCH_ROUTING_KEY = "like.video.batch";
    
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

    @Bean
    public TopicExchange likeExchange() {
        // 设置交换机持久化
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue likeQueue() {
        // 设置队列持久化和懒加载，并配置死信队列
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-queue-mode", "lazy") // 懒加载，适合大量消息存储
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .withArgument("x-message-ttl", 3600000) // 1小时后过期
                .build();
    }

    @Bean
    public Binding likeBinding() {
        return BindingBuilder.bind(likeQueue()).to(likeExchange()).with(ROUTING_KEY);
    }
    
    /**
     * 声明批量点赞队列
     */
    @Bean
    public Queue batchLikeQueue() {
        // 设置队列持久化和懒加载，并配置死信队列
        return QueueBuilder.durable(BATCH_QUEUE)
                .withArgument("x-queue-mode", "lazy") // 懒加载，适合大量消息存储
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .withArgument("x-message-ttl", 3600000) // 1小时后过期
                .build();
    }
    
    /**
     * 绑定批量点赞队列到交换机
     */
    @Bean
    public Binding batchLikeBinding() {
        return BindingBuilder.bind(batchLikeQueue()).to(likeExchange()).with(BATCH_ROUTING_KEY);
    }


    // 定义队列
    @Bean
    public Queue queue() {
        return new Queue("create_walletone_queue", true);  // true 表示持久化
    }

    // 定义交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("create_walletone_exampleExchange");
    }

    // 定义绑定，队列与交换机绑定
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("create_walletone_RoutingKey");
    }






}
