package com.bruce.listener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "YanZhengQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
