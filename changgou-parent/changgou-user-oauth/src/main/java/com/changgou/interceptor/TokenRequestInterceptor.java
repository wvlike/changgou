package com.changgou.interceptor;

import com.changgou.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * package com.changgou.interceptor;
 *
 * @auther txw
 * @create 2019-09-07  21:18
 * @description：
 */
@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {

    /**
     * 从数据库加载查询用户信息
     * 1:没有令牌，Feign调用之前，生成令牌(admin)
     * 2:Feign调用之前，令牌需要携带过去
     * 3:Feign调用之前，令牌需要存放到Header文件中
     * 4:请求->Feign调用->拦截器RequestInterceptor->Feign调用之前执行拦截
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {

        String token = AdminToken.adminToken();
        //给其添加一个请求头
        requestTemplate.header("Authorization","bearer "+token);



    }
}
