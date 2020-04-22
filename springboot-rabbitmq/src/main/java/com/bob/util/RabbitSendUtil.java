package com.bob.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitSendUtil {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 队列设置延时
     * @param routingKey
     * @param message
     * @param isDelay
     */
    public void sendDelayQueue(String routingKey, String message, boolean isDelay) {
        if (isDelay) {
            JSONObject object = JSONUtil.toBean(message, JSONObject.class);
            object.put("queue", routingKey);
            String msg = JSONUtil.toJsonStr(object);
            log.info("延时的队列开始发送消息:{}", msg);
            rabbitTemplate.convertAndSend("dead_letter_queue", msg);
        } else {
            rabbitTemplate.convertAndSend(routingKey, message);
        }
    }

    /**
     * 消息设置延时
     * @param routingKey
     * @param message
     * @param times
     */
    public void sendDelayMessage(String routingKey, String message, long times) {
        JSONObject object = JSONUtil.toBean(message, JSONObject.class);
        object.put("queue", routingKey);
        String msg = JSONUtil.toJsonStr(object);
        log.info("延时的消息开始发送消息:{}", msg);
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(times + "");
                return message;
            }
        };
        rabbitTemplate.convertAndSend("dead_letter_queue_msg", (Object)msg, processor);
    }
}
