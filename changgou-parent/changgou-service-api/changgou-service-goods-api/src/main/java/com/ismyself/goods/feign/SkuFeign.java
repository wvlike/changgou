package com.ismyself.goods.feign;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:txw
 * @Description:sku的feign
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * 批量修改商品库存,商品递增
     *
     * @param iecrmap
     * @return
     */
    @GetMapping("/iecr/count")
    Result iecrCount(@RequestParam Map<String, Object> iecrmap);

    /**
     * 根据封装的条件进行递减商品
     *
     * @param decrmap
     */
    @GetMapping("/decr/count")
    Result decrCount(@RequestParam Map<String, Object> decrmap);


    /**
     * 根据状态获取所有的sku
     *
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);

    /***
     * Sku分页条件搜索实现
     * @param sku
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable int page, @PathVariable int size);

    /***
     * Sku分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    Result delete(@PathVariable Long id);

    /***
     * 修改Sku数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    Result update(@RequestBody Sku sku, @PathVariable Long id);

    /***
     * 新增Sku数据
     * @param sku
     * @return
     */
    @PostMapping
    Result add(@RequestBody Sku sku);

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable Long id);

    /***
     * 查询Sku全部数据
     * @return
     */
    @GetMapping
    Result<List<Sku>> findAll();
}