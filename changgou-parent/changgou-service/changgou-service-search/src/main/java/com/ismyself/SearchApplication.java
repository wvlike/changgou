package com.ismyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-08-29  12:40
 * @description：
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//禁用数据源对象的加载
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.ismyself.goods.feign")//开启feign
@EnableElasticsearchRepositories(basePackages = "com.ismyself.search.dao")//开启ES扫描
public class SearchApplication {

    public static void main(String[] args) {

        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * availableProcessors is already set to [12], rejecting [12]
         ***/
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        SpringApplication.run(SearchApplication.class, args);
    }


}
