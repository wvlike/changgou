package com.ismyself.item.controller;

import com.ismyself.item.service.PageService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * package com.ismyself.item.controller;
 *
 * @auther txw
 * @create 2019-09-03  23:09
 * @description：
 */
@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 根据skuId生成对应商品的HTML页面
     * @param id
     * @return
     */
    @RequestMapping("/createHTML/{id}")
    public Result createHTML(@PathVariable("id") Long id){
        pageService.createPageHTML(id);
        return new Result(true, StatusCode.OK,"ok");
    }

}
