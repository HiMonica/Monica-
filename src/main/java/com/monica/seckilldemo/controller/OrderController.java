package com.monica.seckilldemo.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.IOrderService;
import com.monica.seckilldemo.vo.OrderDetailVo;
import com.monica.seckilldemo.vo.RespBean;
import com.monica.seckilldemo.vo.RespBeanEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean queryDetail(User user,Long orderId){
        if (null == user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo orderDetailVo = orderService.detail(orderId);
        return RespBean.success(orderDetailVo);
    }

}
