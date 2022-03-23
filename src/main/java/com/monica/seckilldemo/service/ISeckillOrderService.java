package com.monica.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.monica.seckilldemo.pojo.SeckillMessage;
import com.monica.seckilldemo.pojo.SeckillOrder;
import com.monica.seckilldemo.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 轮询结果
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
