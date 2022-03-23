package com.monica.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.vo.LoginVo;
import com.monica.seckilldemo.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 秒杀用户表 服务类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
public interface IUserService extends IService<User> {
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
