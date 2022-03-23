package com.monica.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monica.seckilldemo.pojo.Goods;
import com.monica.seckilldemo.vo.GoodsVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("SELECT\n" +
            "\tg.id,\n" +
            "\tg.goods_title,\n" +
            "\tg.goods_stock,\n" +
            "\tg.goods_price,\n" +
            "\tg.goods_name,\n" +
            "\tg.goods_img,\n" +
            "\tg.goods_detail,\n" +
            "\ts.stock_count,\n" +
            "\ts.seckill_price,\n" +
            "\ts.start_date,\n" +
            "\ts.end_date\n" +
            "FROM\n" +
            "\tgoods g\n" +
            "\tLEFT JOIN seckill_goods AS s ON g.id = s.goods_id ")
    List<GoodsVo> findGoodsVo();

    @Select("SELECT\n" +
            "\tg.id,\n" +
            "\tg.goods_title,\n" +
            "\tg.goods_stock,\n" +
            "\tg.goods_price,\n" +
            "\tg.goods_name,\n" +
            "\tg.goods_img,\n" +
            "\tg.goods_detail,\n" +
            "\ts.stock_count,\n" +
            "\ts.seckill_price,\n" +
            "\ts.start_date,\n" +
            "\ts.end_date\n" +
            "FROM\n" +
            "\tgoods g\n" +
            "\tLEFT JOIN seckill_goods AS s ON g.id = s.goods_id where g.id=#{goodsId}")
    GoodsVo findGoodsVoBygoodId(Long goodsId);
}
