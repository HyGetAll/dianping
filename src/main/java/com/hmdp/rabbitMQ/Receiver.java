package com.hmdp.rabbitMQ;

import cn.hutool.json.JSONUtil;
import com.hmdp.config.RabbiMQDirectConfig;

import com.hmdp.entity.VoucherOrder;
import com.hmdp.service.impl.VoucherOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class Receiver {

    @Resource
    private VoucherOrderServiceImpl voucherOrderService;

    @RabbitListener(queues=RabbiMQDirectConfig.QUEUE_NAME)
    public void receiveSekilMessage(VoucherOrder msg){
        log.info("接收到消息: "+msg.toString());
        voucherOrderService.createVoucherOrder(msg);
        log.info("消息保存成功");
    }
}
