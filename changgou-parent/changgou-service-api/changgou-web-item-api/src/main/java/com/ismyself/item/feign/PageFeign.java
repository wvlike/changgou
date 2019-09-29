package com.ismyself.item.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * package com.ismyself.item.feign;
 *
 * @auther txw
 * @create 2019-09-05  14:25
 * @description：
 */
@FeignClient(name = "item")
@RequestMapping("/page")
public interface PageFeign {

    /**
     * 根据SpuId生成静态页面
     * @param id
     * @return
     */
    @RequestMapping("/createHTML/{id}")
    Result createHTML(@PathVariable("id") Long id);

}
