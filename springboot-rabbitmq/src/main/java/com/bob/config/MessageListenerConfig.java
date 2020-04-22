package com.bob.config;

import com.bob.receiver.DirectExchangeReceiver;
import com.bob.receiver.FanoutExchangeReceiver;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MessageListenerConfig {

    private final CachingConnectionFactory connectionFactory;

    // 直连
    private final DirectRabbitConfig directRabbitConfig;
    private final DirectExchangeReceiver directReceiver;//Direct消息接收处理类

    // 扇形
    private final FanoutRabbitConfig fanoutRabbitConfig;
    private final FanoutExchangeReceiver fanoutExchangeReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // NONE: 自动确认  MANUAL:手动确认  AUTO:视情况确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setQueues(fanoutRabbitConfig.queueB());
        container.setMessageListener(fanoutExchangeReceiver);
        return container;
    }
}
