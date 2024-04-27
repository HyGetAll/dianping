package com.hmdp.rabbitMQ;

import cn.hutool.json.JSONUtil;
import com.hmdp.config.RabbiMQDirectConfig;

import com.hmdp.entity.VoucherOrder;
import com.hmdp.service.impl.VoucherOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.channels.Channel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class Receiver {

    @Resource
    private VoucherOrderServiceImpl voucherOrderService;


    @RabbitListener(queues=RabbiMQDirectConfig.QUEUE_NAME)
    public void receiveSekilMessage(VoucherOrder msg){
        log.info("接收到消息: "+msg);
        voucherOrderService.createVoucherOrder(msg);
        log.info("消息消费成功");
    }
}
