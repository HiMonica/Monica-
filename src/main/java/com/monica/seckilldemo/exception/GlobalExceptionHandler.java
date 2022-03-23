package com.monica.seckilldemo.exception;

import com.monica.seckilldemo.vo.RespBean;
import com.monica.seckilldemo.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if (e instanceof GlobalException){
            GlobalException exception = (GlobalException) e;
            return RespBean.error(exception.getRespBeanEnum());
        }else if (e instanceof BindException){
            BindException exception = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_REEOR);
            respBean.setMessage("参数校验异常：" + exception.getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
