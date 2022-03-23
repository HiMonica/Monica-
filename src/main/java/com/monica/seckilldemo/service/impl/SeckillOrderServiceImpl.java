package com.monica.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monica.seckilldemo.mapper.SeckillOrderMapper;
import com.monica.seckilldemo.pojo.SeckillMessage;
import com.monica.seckilldemo.pojo.SeckillOrder;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.ISeckillOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null){
            return seckillOrder.getOrderId();
        }else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)){
            return -1L;
        }else {
            return 0L;
        }
    }
}
