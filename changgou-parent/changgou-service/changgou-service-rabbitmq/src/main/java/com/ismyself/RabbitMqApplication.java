package com.ismyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-09-05  14:32
 * @description：
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//排斥加载数据源对象
@EnableFeignClients//Feign开启
@EnableEurekaClient//注册eureka服务
public class RabbitMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class,args);
    }
}
