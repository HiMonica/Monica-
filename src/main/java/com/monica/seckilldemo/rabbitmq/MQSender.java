package com.monica.seckilldemo.rabbitmq;

import com.monica.seckilldemo.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class MQSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg){
//        log.info("发送消息："+msg);
//        try{
//            rabbitTemplate.convertAndSend(RabbitMQConfig.exchange, RabbitMQConfig.routing, msg);
//        }catch (Exception e){
//            log.error("连接MQ失败", e);
//        }
//
//    }
//
//    public void send(String msg){
//        log.info("发送消息："+msg);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.fanout,"",msg);
//    }
//
//    public void send01(Object msg){
//        log.info("发送消息" + msg);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.direct,RabbitMQConfig.key01,msg);
//    }
//
//    public void send02(Object msg){
//        log.info("发送消息" + msg);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.direct,RabbitMQConfig.key02,msg);
//    }

    public void sendSeckillMessage(String message){
        log.info("发送消息：" + message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"seckill.message",message);
    }

}
