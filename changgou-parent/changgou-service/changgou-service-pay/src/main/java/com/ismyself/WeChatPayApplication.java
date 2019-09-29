package com.ismyself;

import interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-09-09  21:41
 * @description：
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeChatPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeChatPayApplication.class,args);
    }

    /**
     * feign调用前的拦截器
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
