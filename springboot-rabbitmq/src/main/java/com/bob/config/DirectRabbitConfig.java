package com.bob.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认是直连交换机
 */
@Configuration
public class DirectRabbitConfig {

    @Bean
    public Queue testDirectQueue() {
        return new Queue("TestDirectQueue", false);
    }

    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange("TestDirectExchange");
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange()).with("TestDirectRouting");
    }

    // 延时队列实现
    @Bean
    public Queue delayQueue() {
        return new Queue("delay_queue", true);
    }

    @Bean
    public Queue dispatchQueue() {
        return new Queue("dispatch_queue", true);
    }

    // 中转站
    @Bean
    Binding bindDispatchQueue() {
        return BindingBuilder.bind(dispatchQueue()).to(testDirectExchange()).with("dispatch_queue");
    }

    @Bean
    public Queue deadLetterQueue1() {
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl", 5000);
        args.put("x-dead-letter-exchange", "TestDirectExchange");
        args.put("x-dead-letter-routing-key", "dispatch_queue");
        return new Queue("dead_letter_queue", true, false, false, args);
    }

    @Bean
    public Queue deadLetterQueue2() {
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "TestDirectExchange");
        args.put("x-dead-letter-routing-key", "dispatch_queue");
        return new Queue("dead_letter_queue_msg", true, false, false, args);
    }

    // 重试机制
    @Bean
    public Queue retryQueue() {
        return new Queue("retry_queue",false);
    }
}
