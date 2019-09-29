package com.ismyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-08-29  17:20
 * @description：
 */
@SpringBootApplication
@EnableEurekaClient//开启注册中心客户端
@MapperScan(basePackages = "com.ismyself.content.dao")
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
