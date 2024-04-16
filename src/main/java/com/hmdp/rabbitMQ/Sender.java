package com.hmdp.rabbitMQ;

import com.hmdp.config.RabbiMQDirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Sender {
    public RabbitTemplate rabbitTemplate;

    public void sendSeckilMessage(String msg){
        log.info("发送消息：{}",msg);
        rabbitTemplate.convertAndSend(RabbiMQDirectConfig.EXCHANGE_NAME,RabbiMQDirectConfig.BINDING_KEY,msg);
    }
}
