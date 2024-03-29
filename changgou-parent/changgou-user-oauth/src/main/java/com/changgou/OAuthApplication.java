package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/6 8:01
 * @Description: com.changgou
 ****/
@SpringBootApplication
@EnableDiscoveryClient   //开启eureka服务
@MapperScan(basePackages = "com.changgou.auth.dao") //dao扫描包
@EnableFeignClients(basePackages = "com.ismyself.user.feign")  //开启feign支持
public class OAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class,args);
    }

    /**
     * 管理restTemplate，注册
     * @return
     */
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}