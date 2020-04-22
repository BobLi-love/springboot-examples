package com.bob.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bob.util.RabbitSendUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rabbitmq")
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitSendUtil rabbitSendUtil;

    // 测试直连交换机
    @GetMapping("/testDirectExchange2")
    public String testDirectExchange(String msg) {
        // rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", msg);
        rabbitTemplate.convertAndSend("TestDirectQueue", msg);
        return "ok";
    }

    // 测试主题交换机
    @GetMapping("/testTopicExchange")
    public String testTopicExchange(String routingKey, String msg) {
         rabbitTemplate.convertAndSend("TestTopicExchange", routingKey, msg);
        return "ok";
    }

    // 测试扇形交换机（订阅，广播）
    @GetMapping("/testFanoutExchange")
    public String testFanoutExchange(String msg) {
        rabbitTemplate.convertAndSend("TestFanoutExchange", null, msg);
        return "ok";
    }

    // 测试消息回调：1.消息推送到server，但是在server里找不到交换机
    @GetMapping("/testProducerAckMessage1")
    public String testMessage1(String msg) {
        rabbitTemplate.convertAndSend("no_exist_exchange", "TestDirectQueue", msg);
        return "ok";
    }

    // 测试消息回调：2.消息推送到server，找到交换机了，但是没找到队列
    @GetMapping("/testProducerAckMessage2")
    public String testMessage2(String msg) {
        rabbitTemplate.convertAndSend("TestDirectExchange", "11", msg);
        return "ok";
    }
    // 测试消息回调：3.消息推送到server，交换机和队列都找不到 回调结果和1一样
    // 测试消息回调：4.消息推送成功，会调用ConfirmCallback

    // 测试延时队列
    @GetMapping("/testDelayQueue")
    public String testDelayQueue(String routingKey, boolean isDelay) {
        JSONObject object = new JSONObject();
        object.put("name", "bob");
        object.put("age", "18");
        String message = JSONUtil.toJsonStr(object);
        rabbitSendUtil.sendDelayQueue(routingKey, message, isDelay);
        return "ok";
    }

    @GetMapping("/testDelayMessage")
    public String testDelayMessage(String routingKey) {
        JSONObject object = new JSONObject();
        object.put("name", "bob");
        object.put("age", "18");
        String message = JSONUtil.toJsonStr(object);
        rabbitSendUtil.sendDelayMessage(routingKey, message, 3000);
        return "ok";
    }

    @GetMapping("/testRetryQueue")
    public String testRetryQueue(String routingKey, String msg){
        rabbitTemplate.convertAndSend(routingKey, msg);
        return "ok";
    }
}
