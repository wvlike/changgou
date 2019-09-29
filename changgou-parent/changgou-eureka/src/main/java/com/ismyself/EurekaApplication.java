package com.ismyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-08-23  19:00
 * @description：注册中心..
 */
@SpringBootApplication
@EnableEurekaServer//开启注册中心服务
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
