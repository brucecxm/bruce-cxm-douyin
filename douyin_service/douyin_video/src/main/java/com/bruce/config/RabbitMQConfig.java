package com.bruce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 配置Jackson2JsonMessageConverter，用于将对象转换为JSON格式发送
     * 
     * @return Jackson2JsonMessageConverter
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate，设置消息转换器和回调
     * 
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // 设置确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println(
                        "消息发送成功，correlationId: " + (correlationData != null ? correlationData.getId() : "null"));
            } else {
                System.err.println("消息发送失败，原因: " + cause + ", correlationId: "
                        + (correlationData != null ? correlationData.getId() : "null"));
            }
        });

        // 设置返回回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.err.println("消息发送到队列失败: ");
            System.err.println("  消息: " + message);
            System.err.println("  回复码: " + replyCode);
            System.err.println("  回复文本: " + replyText);
            System.err.println("  交换机: " + exchange);
            System.err.println("  路由键: " + routingKey);
        });

        // 启用mandatory模式，确保消息发送失败时返回
        rabbitTemplate.setMandatory(true);

        return rabbitTemplate;
    }

    /**
     * 配置默认简单队列
     * 
     * @return Queue
     */
    @Bean
    public Queue defaultSimpleQueue() {
        return QueueBuilder.durable("default-simple-queue")
                .withArgument("x-queue-mode", "lazy")
                .withArgument("x-message-ttl", 3600000) // 1小时过期
                .build();
    }

    /**
     * 配置默认对象队列
     * 
     * @return Queue
     */
    @Bean
    public Queue defaultObjectQueue() {
        return QueueBuilder.durable("default-object-queue")
                .withArgument("x-queue-mode", "lazy")
                .withArgument("x-message-ttl", 3600000) // 1小时过期
                .build();
    }

    /**
     * 配置默认批量队列
     * 
     * @return Queue
     */
    @Bean
    public Queue defaultBatchQueue() {
        return QueueBuilder.durable("default-batch-queue")
                .withArgument("x-queue-mode", "lazy")
                .withArgument("x-message-ttl", 3600000) // 1小时过期
                .build();
    }

    /**
     * 配置默认Map队列
     * 
     * @return Queue
     */
    @Bean
    public Queue defaultMapQueue() {
        return QueueBuilder.durable("default-map-queue")
                .withArgument("x-queue-mode", "lazy")
                .withArgument("x-message-ttl", 3600000) // 1小时过期
                .build();
    }

    /**
     * 配置默认死信队列
     * 
     * @return Queue
     */
    @Bean
    public Queue defaultDeadLetterQueue() {
        return QueueBuilder.durable("default-dead-letter-queue")
                .withArgument("x-queue-mode", "lazy")
                .build();
    }

    /**
     * 配置默认交换机
     * 
     * @return TopicExchange
     */
    @Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange("default.exchange", true, false);
    }

    /**
     * 配置死信交换机
     * 
     * @return DirectExchange
     */
    @Bean
    public DirectExchange defaultDeadLetterExchange() {
        return new DirectExchange("default.dead.letter.exchange", true, false);
    }

    /**
     * 绑定默认简单队列到默认交换机
     * 
     * @return Binding
     */
    @Bean
    public Binding bindDefaultSimpleQueue() {
        return BindingBuilder.bind(defaultSimpleQueue())
                .to(defaultExchange())
                .with("default.simple.#");
    }

    /**
     * 绑定默认对象队列到默认交换机
     * 
     * @return Binding
     */
    @Bean
    public Binding bindDefaultObjectQueue() {
        return BindingBuilder.bind(defaultObjectQueue())
                .to(defaultExchange())
                .with("default.object.#");
    }

    /**
     * 绑定默认批量队列到默认交换机
     * 
     * @return Binding
     */
    @Bean
    public Binding bindDefaultBatchQueue() {
        return BindingBuilder.bind(defaultBatchQueue())
                .to(defaultExchange())
                .with("default.batch.#");
    }

    /**
     * 绑定默认Map队列到默认交换机
     * 
     * @return Binding
     */
    @Bean
    public Binding bindDefaultMapQueue() {
        return BindingBuilder.bind(defaultMapQueue())
                .to(defaultExchange())
                .with("default.map.#");
    }

    /**
     * 绑定默认死信队列到默认死信交换机
     * 
     * @return Binding
     */
    @Bean
    public Binding bindDefaultDeadLetterQueue() {
        return BindingBuilder.bind(defaultDeadLetterQueue())
                .to(defaultDeadLetterExchange())
                .with("default.dead.letter");
    }

}