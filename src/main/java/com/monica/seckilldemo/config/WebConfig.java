package com.monica.seckilldemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author liuyy
 * @version 1.0
 * @datetime
 * @decription WebMvc配置类
 **/
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    /**
     *  视图跳转控制器
     * 无业务逻辑的跳转 均可以以这种方法写在这里
     * @param registry
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
    }

    //最主要的是在这里
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //告知系统static 当成 静态资源访问,要去获取target下面的classes下面的static下面的资源
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}

