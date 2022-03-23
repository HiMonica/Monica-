package com.monica.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monica.seckilldemo.mapper.GoodsMapper;
import com.monica.seckilldemo.pojo.Goods;
import com.monica.seckilldemo.service.IGoodsService;
import com.monica.seckilldemo.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoBygoodId(Long goodsId) {
        return goodsMapper.findGoodsVoBygoodId(goodsId);
    }
}
