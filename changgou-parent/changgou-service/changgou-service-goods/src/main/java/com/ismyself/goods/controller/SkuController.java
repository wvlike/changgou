package com.ismyself.goods.controller;


import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Sku;
import com.ismyself.goods.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:txw
 * @Description:
 */

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 批量修改商品库存,商品递增
     *
     * @param iecrmap
     * @return
     */
    @GetMapping("/iecr/count")
    public Result iecrCount(@RequestParam Map<String, Object> iecrmap) {
        try {
            skuService.iecrCount(iecrmap);
            return new Result<List<Sku>>(true, StatusCode.OK, "商品递增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<Sku>>(false, StatusCode.ERROR, "商品递增失败");
        }
    }


    /**
     * 批量修改商品库存,商品递减
     *
     * @param decrmap
     * @return
     */
    @GetMapping("/decr/count")
    public Result decrCount(@RequestParam Map<String, Object> decrmap) {
//        try {
            skuService.decrCount(decrmap);
            return new Result<List<Sku>>(true, StatusCode.OK, "商品递减成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result<List<Sku>>(false, StatusCode.ERROR, "商品递减失败");
//        }
    }


    /**
     * 根据审核状态查询
     *
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable("status") String status) {
        List<Sku> skus = skuService.findByStatus(status);
        return new Result<List<Sku>>(true, StatusCode.OK, "根据状态查询成功", skus);
    }

    /***
     * Sku分页条件搜索实现
     * @param sku
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable int page, @PathVariable int size) {
        //调用SkuService实现分页条件查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(sku, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * Sku分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //调用SkuService实现分页查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku) {
        //调用SkuService实现条件查询Sku
        List<Sku> list = skuService.findList(sku);
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        //调用SkuService实现根据主键删除
        skuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改Sku数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Sku sku, @PathVariable Long id) {
        //设置主键值
        sku.setId(id);
        //调用SkuService实现修改Sku
        skuService.update(sku);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增Sku数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Sku sku) {
        //调用SkuService实现添加Sku
        skuService.add(sku);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable Long id) {
        //调用SkuService实现根据主键查询Sku
        Sku sku = skuService.findById(id);
        return new Result<Sku>(true, StatusCode.OK, "查询成功", sku);
    }

    /***
     * 查询Sku全部数据
     * @return
     */
    @GetMapping
    public Result<List<Sku>> findAll() {
        //调用SkuService实现查询所有Sku
        List<Sku> list = skuService.findAll();
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", list);
    }
}
