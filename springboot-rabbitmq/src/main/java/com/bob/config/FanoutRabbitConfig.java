package com.bob.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 因为是扇型交换机, 路由键无需配置,配置也不起作用
 */
@Configuration
public class FanoutRabbitConfig {

    @Bean
    public Queue queueA() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue queueB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue queueC() {
        return new Queue("fanout.C");
    }

    @Bean
    FanoutExchange testFanoutExchange() {
        return new FanoutExchange("TestFanoutExchange");
    }

    @Bean
    Binding bindQueueA() {
        return BindingBuilder.bind(queueA()).to(testFanoutExchange());
    }

    @Bean
    Binding bindQueueB() {
        return BindingBuilder.bind(queueB()).to(testFanoutExchange());
    }

    @Bean
    Binding bindQueueC() {
        return BindingBuilder.bind(queueC()).to(testFanoutExchange());
    }
}
