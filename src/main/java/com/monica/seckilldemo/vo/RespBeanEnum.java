package com.monica.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum RespBeanEnum {
    //通用
    SUCCESS("200","SUCCESS"),
    ERROR("500","服务器异常"),
    //登录模块5002xx
    LOGIN_ERROR("500210","账号或密码错误"),
    MOBILE_REEOR("500211","手机号格式不对"),
    BIND_REEOR("500212","参数校验异常"),
    MOBILE_NOT_EXIST("500213","手机号不存在"),
    PASSWORD_UPDATE_FAIL("500214","密码更新失败"),
    SESSION_ERROR("500215","用户不存在"),
    //秒杀模块5005xx
    EMPTY_STOCK("500500","库存不足"),
    REPEATE_ERROR("5005001","该商品限购一件"),
    //订单模块
    ORDER_NOT_EXIST("500300","订单信息不存在")
    ;

    private String code;
    private String msg;
}
