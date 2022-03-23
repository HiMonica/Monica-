package com.monica.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monica.seckilldemo.exception.GlobalException;
import com.monica.seckilldemo.mapper.OrderMapper;
import com.monica.seckilldemo.pojo.Order;
import com.monica.seckilldemo.pojo.SeckillGoods;
import com.monica.seckilldemo.pojo.SeckillOrder;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.IGoodsService;
import com.monica.seckilldemo.service.IOrderService;
import com.monica.seckilldemo.service.ISeckillGoodsService;
import com.monica.seckilldemo.service.ISeckillOrderService;
import com.monica.seckilldemo.vo.GoodsVo;
import com.monica.seckilldemo.vo.OrderDetailVo;
import com.monica.seckilldemo.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private ISeckillGoodsService seckillGoodsService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private ISeckillOrderService seckillOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Order secill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(goods.getStockCount()-1);
        boolean updateResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count-1").eq(
                "goods_id", goods.getId()).gt("stock_count", 0)
        );
        if (seckillGoods.getStockCount() < 1){
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(),"0");
            return null;
        }
        //生产订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());
        orderMapper.insert(order);
        //生产秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        valueOperations.set("order:" + user.getId() + ":" + goods.getId(),seckillOrder);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (ObjectUtils.isEmpty(orderId)){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoBygoodId(order.getGoodsId());
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsVo);
        return orderDetailVo;
    }
}
