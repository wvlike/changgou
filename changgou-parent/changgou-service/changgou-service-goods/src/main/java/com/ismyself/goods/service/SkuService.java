package com.ismyself.goods.service;


import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Sku;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author:txw
 * @Description:Sku业务层接口
 */
public interface SkuService {

    /**
     * 根据封装的条件进行递增商品
     * @param iecrmap
     */
    void iecrCount(Map<String,Object> iecrmap);


    /**
     * 根据封装的条件进行递减商品
     * @param decrmap
     */
    void decrCount(Map<String,Object> decrmap);


    /**
     * 根据状态查询sku
     * @param status
     * @return
     */
    List<Sku> findByStatus(String status);

    /***
     * Sku多条件分页查询
     * @param sku
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(Sku sku, int page, int size);

    /***
     * Sku分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(int page, int size);

    /***
     * Sku多条件搜索方法
     * @param sku
     * @return
     */
    List<Sku> findList(Sku sku);

    /***
     * 删除Sku
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Sku数据
     * @param sku
     */
    void update(Sku sku);

    /***
     * 新增Sku
     * @param sku
     */
    void add(Sku sku);

    /**
     * 根据ID查询Sku
     * @param id
     * @return
     */
     Sku findById(Long id);

    /***
     * 查询所有Sku
     * @return
     */
    List<Sku> findAll();
}
