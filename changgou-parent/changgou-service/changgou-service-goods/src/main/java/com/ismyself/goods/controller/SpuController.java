package com.ismyself.goods.controller;


import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Goods;
import com.ismyself.goods.pojo.Spu;
import com.ismyself.goods.service.SpuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:txw
 * @Description:
 */

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    /**
     * 商品逻辑删除
     * @param id
     * @return
     */
    @PutMapping("/logic/delete/{id}")
    public Result<Goods> logicDelete(@PathVariable("id") Long id) {
        spuService.logicDelete(id);
        return new Result<Goods>(true, StatusCode.OK, "商品逻辑删除成功");
    }

    /**
     * 商品逻辑还原
     * @param id
     * @return
     */
    @PutMapping("/restore/{id}")
    public Result<Goods> restore(@PathVariable("id") Long id) {
        spuService.restore(id);
        return new Result<Goods>(true, StatusCode.OK, "商品逻辑还原成功");
    }

    /**
     * 商品批量下架
     *
     * @param ids
     * @return
     */
    @PutMapping("/pull/many")
    public Result<Goods> pullmay(@RequestBody Long[] ids) {
        spuService.pullmany(ids);
        return new Result<Goods>(true, StatusCode.OK, "商品批量下架成功");
    }


    /**
     * 商品批量上架
     *
     * @param ids
     * @return
     */
    @PutMapping("/put/many")
    public Result<Goods> putmay(@RequestBody Long[] ids) {
        spuService.putmany(ids);
        return new Result<Goods>(true, StatusCode.OK, "商品批量上架成功");
    }

    /**
     * 商品下架
     *
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")
    public Result<Goods> pull(@PathVariable("id") Long id) {
        spuService.pull(id);
        return new Result<Goods>(true, StatusCode.OK, "商品下架成功");
    }

    /**
     * 商品上架
     *
     * @param id
     * @return
     */
    @PutMapping("/put/{id}")
    public Result<Goods> put(@PathVariable("id") Long id) {
        spuService.put(id);
        return new Result<Goods>(true, StatusCode.OK, "商品上架成功");
    }

    /**
     * 商品审核
     *
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result<Goods> audit(@PathVariable("id") Long id) {
        spuService.audit(id);
        return new Result<Goods>(true, StatusCode.OK, "商品审核成功");
    }

    /**
     * 保存商品
     *
     * @param goods
     * @return
     */
    @PostMapping("/goods")
    public Result<Goods> saveGoods(@RequestBody Goods goods) {
        spuService.saveGoods(goods);
        return new Result<Goods>(true, StatusCode.OK, "保存商品成功");
    }

    /**
     * 根据spuId获取商品
     *
     * @param spuId
     * @return
     */
    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodById(@PathVariable("id") Long spuId) {
        Goods goods = spuService.findGoodsById(spuId);
        return new Result<Goods>(true, StatusCode.OK, "根据spuId获取商品成功", goods);
    }

    /**
     * 修改商品
     *
     * @param goods
     * @return
     */
    @PutMapping("/goods")
    public Result<Goods> updateGoods(@RequestBody Goods goods) {
        spuService.saveGoods(goods);
        return new Result<Goods>(true, StatusCode.OK, "修改商品成功");
    }

    /***
     * Spu分页条件搜索实现
     * @param spu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Spu spu, @PathVariable int page, @PathVariable int size) {
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * Spu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Spu>> findList(@RequestBody(required = false) Spu spu) {
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Spu spu, @PathVariable Long id) {
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spu spu) {
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id) {
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true, StatusCode.OK, "查询成功", spu);
    }

    /***
     * 查询Spu全部数据
     * @return
     */
    @GetMapping
    public Result<List<Spu>> findAll() {
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }
}
