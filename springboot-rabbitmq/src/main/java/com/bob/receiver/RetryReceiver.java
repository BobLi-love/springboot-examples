package com.bob.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryReceiver {

    @RabbitListener(queues = "retry_queue")
    public void retryReceive(String message) {
        log.info("重试机制接收消息:{}", message);
        try {
//            int num = 1/0;
        } catch (Exception e) {
            log.error("重试机制异常:{}", e.getMessage());
            throw e; //一定要抛出异常
        }
    }
}
