package com.ismyself.item.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * package com.ismyself.item.config;
 *
 * @auther txw
 * @create 2019-09-04  15:29
 * @description：
 */
@ControllerAdvice
@Configuration
public class EnableMvcConfig implements WebMvcConfigurer {
    /***
     * 静态资源放行
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/items/**").addResourceLocations("classpath:/templates/items/");
    }
}
