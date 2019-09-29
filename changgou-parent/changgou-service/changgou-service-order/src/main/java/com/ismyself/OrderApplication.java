package com.ismyself;

import entity.IdWorker;
import interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-09-07  19:11
 * @description：
 */

@SpringBootApplication
@EnableEurekaClient     //开启eureka服务
@MapperScan(basePackages = {"com.ismyself.order.dao"})
@EnableFeignClients(basePackages = {"com.ismyself.goods.feign","com.ismyself.user.feign","com.ismyself.pay.feign"})  //开启feign客户端
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }


    /**
     * feign调用前的拦截器
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

}