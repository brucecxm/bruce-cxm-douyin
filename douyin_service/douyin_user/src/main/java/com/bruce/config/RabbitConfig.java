package com.bruce.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("YanZhengQueue", true);
    }








    // 定义队列
    @Bean
    public Queue queue() {
        return new Queue("create_wallet_queue", true);  // true 表示持久化
    }

    // 定义交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("create_wallet_exampleExchange");
    }

    // 定义绑定，队列与交换机绑定
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("create_wallet_RoutingKey");
    }






}
