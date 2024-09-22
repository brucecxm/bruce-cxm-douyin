package com.bruce.config;import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机
    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";
    // 普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列
    public static final String DEAD_LETTER_QUEUE = "dead_letter_queue";

    // 普通交换机
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 普通队列
    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(NORMAL_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE) // 设置死信交换机
                .withArgument("x-dead-letter-routing-key", "dead") // 设置死信路由键
                .withArgument("x-message-ttl", 60000) // 设置TTL，单位为毫秒
                .build();
    }

    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 普通队列绑定到普通交换机
    @Bean
    public Binding normalBinding(Queue normalQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("normal");
    }

    // 死信队列绑定到死信交换机
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("dead");
    }
}
