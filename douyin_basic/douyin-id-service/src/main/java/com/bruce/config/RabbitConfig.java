package com.bruce.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("YanZhengQueue", true);
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

    public static final String EXCHANGE = "like.exchange";
    public static final String QUEUE = "like.video.queue";
    public static final String ROUTING_KEY = "like.video";

    @Bean
    public TopicExchange likeExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue likeQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding likeBinding() {
        return BindingBuilder.bind(likeQueue()).to(likeExchange()).with(ROUTING_KEY);
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
