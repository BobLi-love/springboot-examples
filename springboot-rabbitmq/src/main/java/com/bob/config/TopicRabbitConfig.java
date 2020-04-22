package com.bob.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    public static final String MAN = "topic.man";
    public static final String WOMAN = "topic.woman";


    @Bean
    public Queue manQueue() {
        return new Queue("manQueue", true);
    }

    @Bean
    public Queue womanQueue() {
        return new Queue("womanQueue", true);
    }

    @Bean
    TopicExchange testTopicExchane() {
        return new TopicExchange("TestTopicExchange");
    }

    @Bean
    Binding bindManQueue() {
        return BindingBuilder.bind(manQueue()).to(testTopicExchane()).with(MAN);
    }

    @Bean
    Binding bindWomanQueue() {
        return BindingBuilder.bind(womanQueue()).to(testTopicExchane()).with("topic.#");
    }
}
