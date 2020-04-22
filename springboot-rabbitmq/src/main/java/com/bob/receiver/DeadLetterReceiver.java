package com.bob.receiver;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DeadLetterReceiver {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "dispatch_queue")
    public void dispatchReceive(String message) {
        JSONObject jsonObject = JSONUtil.toBean(message, JSONObject.class);
        String queue = jsonObject.get("queue").toString();
        jsonObject.remove("queue");
        String msg = JSONUtil.toJsonStr(jsonObject);
        rabbitTemplate.convertAndSend(queue, msg);
    }

    @RabbitListener(queues = "delay_queue")
    public void delayReceive(String msg) {
        log.info("延时接收消息:{}", msg);
    }
}
