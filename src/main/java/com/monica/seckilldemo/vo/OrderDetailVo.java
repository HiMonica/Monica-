package com.monica.seckilldemo.vo;

import com.monica.seckilldemo.pojo.Order;
import lombok.Data;

@Data
public class OrderDetailVo {

    private Order order;

    private GoodsVo goodsVo;
}
