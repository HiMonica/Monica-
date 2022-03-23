package com.monica.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.monica.seckilldemo.pojo.Goods;
import com.monica.seckilldemo.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoBygoodId(Long goodsId);
}
