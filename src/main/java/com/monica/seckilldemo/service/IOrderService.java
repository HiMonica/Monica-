package com.monica.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.monica.seckilldemo.pojo.Order;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.vo.GoodsVo;
import com.monica.seckilldemo.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
public interface IOrderService extends IService<Order> {

    Order secill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
