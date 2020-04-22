package com.bob.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FanoutExchangeReceiver implements ChannelAwareMessageListener {

    @RabbitListener(queues = "fanout.A")
    public void receiveQueueA(String msg) {
        log.info("QueueA 接收消息:{}", msg);
    }

    @RabbitListener(queues = "fanout.B")
    public void receiveQueueB(String msg) {
        log.info("QueueB 接收消息:{}", msg);
    }

    @RabbitListener(queues = "fanout.C")
    public void receiveQueueC(String msg) {
        log.info("QueueC 接收消息:{}", msg);
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String messageStr = message.toString();
            String msg = messageStr.split("'")[1];
//            int num = 1/0;
            log.info("扇形交换机监听消息:{}", msg);
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, true); // true会重新放回队列
            e.printStackTrace();
        }
    }
}
