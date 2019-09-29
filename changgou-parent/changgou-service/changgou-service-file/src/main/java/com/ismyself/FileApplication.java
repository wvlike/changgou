package com.ismyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-08-24  17:08
 * @description：
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}) // 禁止数据源对象自动加载
@EnableEurekaClient
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class);
    }
}
