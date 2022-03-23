package com.monica.seckilldemo.vo;

import com.monica.seckilldemo.pojo.User;
import lombok.Data;

@Data
public class DetailVo {

    private User user;

    private GoodsVo goodsVo;

    private int remainSeconds;

    private int seckillStatus;
}
