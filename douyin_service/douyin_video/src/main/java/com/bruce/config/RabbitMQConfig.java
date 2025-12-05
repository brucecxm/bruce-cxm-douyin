package com.bruce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.AcknowledgeMode;
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

        // todo 消息已成功到达交换机（生产者 ConfirmCallback 已返回 ack=true）；
        // todo 消息已成功到达交换机（生产者 ConfirmCallback 已返回 ack=true）；
        // todo 消息已成功到达交换机（生产者 ConfirmCallback 已返回 ack=true）；
        // todo 消息已成功到达交换机（生产者 ConfirmCallback 已返回 ack=true）；
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
        // todo 触发 ReturnsCallback：说明消息未到达队列（路由失败）；
        // todo 触发 ReturnsCallback：说明消息未到达队列（路由失败）；
        // todo 触发 ReturnsCallback：说明消息未到达队列（路由失败）；
        // todo 未触发 ReturnsCallback：说明消息已成功路由到至少一个队列（只要有匹配队列，就不会触发）。//todo 触发
        // ReturnsCallback：说明消息未到达队列（路由失败）；
        // todo 未触发 ReturnsCallback：说明消息已成功路由到至少一个队列（只要有匹配队列，就不会触发）。//todo 触发
        // ReturnsCallback：说明消息未到达队列（路由失败）；
        // todo 未触发 ReturnsCallback：说明消息已成功路由到至少一个队列（只要有匹配队列，就不会触发）。
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            //
            // todo 不管是 “交换机→队列”，还是 “直接发默认交换机（空字符串）到队列”，只要消息没找到目标队列 + mandatory=true，就会触发
            // ReturnCallback；
            // todo 如果 mandatory=false（默认），RabbitMQ 会直接丢弃路由失败的消息，ReturnCallback 不会触发。
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
     * 配置监听器容器工厂，确保手动确认模式被正确设置
     * 
     * @return SimpleRabbitListenerContainerFactory
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        // 明确设置手动确认模式，不受其他配置影响
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置并发消费者数量
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        // 设置预取计数
        factory.setPrefetchCount(100);
        return factory;
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