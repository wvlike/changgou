package com.ismyself.search.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * package com.ismyself.search.feign;
 *
 * @auther txw
 * @create 2019-09-02  20:40
 * @description：
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SkuFeign {

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    Map search(@RequestParam(required = false) Map searchMap);

}
