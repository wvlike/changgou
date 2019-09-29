package interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * package com.changgou.interceptor;
 *
 * @auther txw
 * @create 2019-09-07  21:18
 * @description：
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 从数据库加载查询用户信息
     * 1:没有令牌，Feign调用之前，生成令牌(admin)
     * 2:Feign调用之前，令牌需要携带过去
     * 3:Feign调用之前，令牌需要存放到Header文件中
     * 4:请求->Feign调用->拦截器RequestInterceptor->Feign调用之前执行拦截
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {

        /**
         * 将令牌封装到头文件中
         */
        //使用RequestContextHolder工具获取request相关变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null){
            //取出request
            HttpServletRequest request = attributes.getRequest();
            //获取所有头文件的信息
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null){
                while (headerNames.hasMoreElements()){
                    //请求头的key
                    String headerKey = headerNames.nextElement();
                    //请求头的值
                    String headerValue = request.getHeader(headerKey);
                    requestTemplate.header(headerKey,headerValue);
                }
            }
        }
    }
}
