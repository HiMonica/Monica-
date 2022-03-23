package com.monica.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monica.seckilldemo.exception.GlobalException;
import com.monica.seckilldemo.mapper.UserMapper;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.service.IUserService;
import com.monica.seckilldemo.utils.CookieUtils;
import com.monica.seckilldemo.utils.MD5Utils;
import com.monica.seckilldemo.utils.UUIDUtils;
import com.monica.seckilldemo.vo.LoginVo;
import com.monica.seckilldemo.vo.RespBean;
import com.monica.seckilldemo.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 秒杀用户表 服务实现类
 * </p>
 *
 * @author liuyuyang
 * @since 2021-10-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        /*使用jsr303校验，以下代码可以去掉了
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        // 验证手机号
        if (!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/
        // 根据手机号获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", mobile);
        User user = userMapper.selectOne(wrapper);
        // 用户不存在
        if (null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 密码是否正确
        if (!MD5Utils.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成Cookie
        String ticket = UUIDUtils.uuid();
        //request.getSession().setAttribute(ticket, user);
        redisTemplate.opsForValue().set("user:"+ticket, user);
        CookieUtils.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }

    /**
     * 从cookie中获取密码
     *
     * @param userTicket
     * @param request
     * @param response
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if (user != null){// 以防万一的操作(当userTicket在Redis中设置了有效时间时，如果在登录状态，我们是不允许userTicket失效的，以防万一每次请求都设置一下)
            CookieUtils.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /**
     * 更新密码
     *
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if (null == user){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Utils.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if (1 == result){
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
