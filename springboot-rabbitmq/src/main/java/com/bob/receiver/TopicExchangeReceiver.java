package com.bob.receiver;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicExchangeReceiver {

    @RabbitListener(queues = "manQueue")
    public void receiveManQueue(String msg) {
        log.info("man queue 接收消息:{}", msg);
    }

    @RabbitListener(queues = "womanQueue")
    public void receiveWomanQueue(String msg) {
        log.info("woman queue 接收消息:{}", msg);
    }
}
