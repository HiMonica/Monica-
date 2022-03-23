package com.monica.seckilldemo.rabbitmq;

import com.monica.seckilldemo.config.RabbitMQConfig;
import com.monica.seckilldemo.pojo.SeckillMessage;
import com.monica.seckilldemo.pojo.SeckillOrder;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.IGoodsService;
import com.monica.seckilldemo.service.IOrderService;
import com.monica.seckilldemo.utils.JSONUtils;
import com.monica.seckilldemo.vo.GoodsVo;
import com.monica.seckilldemo.vo.OrderDetailVo;
import com.monica.seckilldemo.vo.RespBean;
import com.monica.seckilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class MQReceiver {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IOrderService orderService;

    @Resource
    private RedisTemplate redisTemplate;

//    @RabbitListener(queues = RabbitMQConfig.queue)
//    public void receive(Object msg){
//        log.info("接受消息：" + msg);
//    }
//
//    @RabbitListener(queues = RabbitMQConfig.queue01)
//    public void receive01(String msg){
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = RabbitMQConfig.queue02)
//    public void receive02(String msg){
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = RabbitMQConfig.queue03)
//    public void receive03(Object msg){
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = RabbitMQConfig.queue04)
//    public void receive04(Object msg){
//        log.info("接收消息：" + msg);
//    }

    @RabbitListener(queues = RabbitMQConfig.QUEUQ)
    public void receiveSeckillMessage(String message){
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JSONUtils.parse(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoBygoodId(goodsId);
        //判断库存
        if (goodsVo.getStockCount() < 1){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            return;
        }
        //下单操作
        orderService.secill(user,goodsVo);
    }
}
