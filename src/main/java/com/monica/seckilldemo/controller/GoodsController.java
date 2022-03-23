package com.monica.seckilldemo.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.IGoodsService;
import com.monica.seckilldemo.service.IUserService;
import com.monica.seckilldemo.vo.DetailVo;
import com.monica.seckilldemo.vo.GoodsVo;
import com.monica.seckilldemo.vo.RespBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private IUserService userService;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
//        if (StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//
//        TUser user = (TUser) session.getAttribute(ticket);
//        TUser user = userService.getUserByCookie(ticket, request, response);
//        if (null == user){
//            return "login";
//        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //如果为空手动渲染，存入Redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoBygoodId(goodsId);
        LocalDateTime startDate = goodsVo.getStartDate();
        LocalDateTime endDate = goodsVo.getEndDate();
        LocalDateTime nowData = LocalDateTime.now();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if (nowData.isBefore(startDate)){
            remainSeconds = (int) (startDate.toEpochSecond(ZoneOffset.of("+8"))-nowData.toEpochSecond(ZoneOffset.of("+8")));
        }else if (nowData.isAfter(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSeckillStatus(seckillStatus);
        return RespBean.success(detailVo);
    }

    // 优化前 QPS：149.51
    // URL缓存优化后 QPS: 261.7
    // goods_detail页面静态化优化后 QPS:461.1
    @RequestMapping("/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail2(User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoBygoodId(goodsId);
        LocalDateTime startDate = goodsVo.getStartDate();
        LocalDateTime endDate = goodsVo.getEndDate();
        LocalDateTime nowData = LocalDateTime.now();

        // 秒杀状态
        int seckillStatus = 0;
        // 秒杀倒计时时间
        int remainSeconds = 0;

        if (nowData.isBefore(startDate)){
            remainSeconds = (int) (startDate.toEpochSecond(ZoneOffset.of("+8"))-nowData.toEpochSecond(ZoneOffset.of("+8")));
        }else if (nowData.isAfter(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 0;
        }

        DetailVo goodsDetailVo = new DetailVo();
        goodsDetailVo.setUser(user);
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(goodsDetailVo);
    }

    @RequestMapping(value = "/toDetail1/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail1(Model model, User user, HttpServletRequest request, HttpServletResponse response, @PathVariable Long goodsId){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoBygoodId(goodsId);
        LocalDateTime startDate = goodsVo.getStartDate();
        LocalDateTime endDate = goodsVo.getEndDate();
        LocalDateTime nowData = LocalDateTime.now();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if (nowData.isBefore(startDate)){
            remainSeconds = (int) (startDate.toEpochSecond(ZoneOffset.of("+8"))-nowData.toEpochSecond(ZoneOffset.of("+8")));
        }else if (nowData.isAfter(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("goods",goodsVo);
        WebContext context = new WebContext(request,response,request.getServletContext(),response.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        valueOperations.set("goodsDetail:" + goodsId,html,60,TimeUnit.SECONDS);
        return html;
    }
}
