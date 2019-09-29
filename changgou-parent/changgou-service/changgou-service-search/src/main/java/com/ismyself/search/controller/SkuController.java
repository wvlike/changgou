package com.ismyself.search.controller;

import com.ismyself.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * package com.ismyself.search.controller;
 *
 * @auther txw
 * @create 2019-08-30  20:25
 * @description：SkuES操作的controller
 */
@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 导入数据到索引库
     * @return
     */
    @GetMapping("/import")
    public Result importData(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到索引库成功");
    }

    /**
     * 根据关键字查询
     * @param map
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String,String> map){
       return skuService.search(map);
    }

}
