package com.monica.seckilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.monica.seckilldemo.pojo.Order;
import com.monica.seckilldemo.pojo.SeckillMessage;
import com.monica.seckilldemo.pojo.SeckillOrder;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.rabbitmq.MQSender;
import com.monica.seckilldemo.service.IGoodsService;
import com.monica.seckilldemo.service.IOrderService;
import com.monica.seckilldemo.service.ISeckillOrderService;
import com.monica.seckilldemo.utils.JSONUtils;
import com.monica.seckilldemo.vo.GoodsVo;
import com.monica.seckilldemo.vo.RespBean;
import com.monica.seckilldemo.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private ISeckillOrderService seckillOrderService;

    @Resource
    private IOrderService orderService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MQSender mqSender;

    private static Map<Long,Boolean> EmptyStockMap = new HashMap<>();

    @PostConstruct
    public void initRedisStockCount(){
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo ->{
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }

    //静态页面
    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId){
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
//        GoodsVo goods = goodsService.findGoodsVoBygoodId(goodsId);
//        //不能使用前端传过来的库存，要去数据库查真实的库存
//        if (goods.getStockCount() < 1){
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).
//                eq("goods_id",goods.getId()));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记，减少redis的访问
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if (stock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //订单
        SeckillMessage seckillMessage = new SeckillMessage(goodsId,user);
        mqSender.sendSeckillMessage(JSONUtils.serialize(seckillMessage));
//        Order order = orderService.secill(user,goods);
        return RespBean.success(0);
    }

    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean result(User user,Long goodsId){
        Long orderId = seckillOrderService.getResult(user,goodsId);
        if (orderId == null){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        return RespBean.success(orderId);
    }

    @RequestMapping(value = "/doSeckill1",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill1(Model model, User user, Long goodsId){
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoBygoodId(goodsId);
        //不能使用前端传过来的库存，要去数据库查真实的库存
        if (goods.getStockCount() < 1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMsg());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).
                eq("goods_id",goods.getId()));
        if (seckillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMsg());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //订单
        Order order = orderService.secill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return RespBean.success(order);
    }
}
