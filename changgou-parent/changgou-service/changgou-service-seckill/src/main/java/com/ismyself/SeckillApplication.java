package com.ismyself;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-09-11  21:15
 * @description：
 */
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling   //开启定时器
@EnableFeignClients(basePackages = {"com.ismyself.goods.feign","com.ismyself.pay.feign"})
@MapperScan(basePackages = {"com.ismyself.seckill.dao"})
@EnableAsync    //开启多线程
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

}
