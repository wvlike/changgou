package com.ismyself;


import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-08-29  17:08
 * @description：
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//排斥数据源对象加载
@EnableEurekaClient//开启注册中心客户端
@EnableCanalClient//开启广告客户端
@EnableFeignClients//开启feign
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class, args);
    }


}
