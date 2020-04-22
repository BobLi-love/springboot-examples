package com.bob.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectExchangeReceiver implements ChannelAwareMessageListener {

    @RabbitListener(queues = "TestDirectQueue")
    public void receiveDirectExchange(String msg) {
        System.out.println(String.format("DirectExchange接收消息:%s", msg));
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String messageStr = message.toString();
            String[] messageArr = messageStr.split("'");
            String msg = messageArr[1];
            log.info("监听消息:{}", msg);
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            // channel.basicReject(deliveryTag, true);  //true会重新放回队列
            e.printStackTrace();
        }
    }
}
