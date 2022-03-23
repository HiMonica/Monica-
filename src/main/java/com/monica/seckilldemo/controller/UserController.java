package com.monica.seckilldemo.controller;


import com.monica.seckilldemo.rabbitmq.MQSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 秒杀用户表 前端控制器
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private MQSender mqSender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq02(){
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mqDirect01(){
//        mqSender.send01("hello red");
//    }
//
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mqDirect02(){
//        mqSender.send02("hello green");
//    }
}
