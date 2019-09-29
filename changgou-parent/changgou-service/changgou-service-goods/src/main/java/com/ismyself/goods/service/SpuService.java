package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Goods;
import com.ismyself.goods.pojo.Spu;

import java.util.List;

/**
 * @Author:txw
 * @Description:Spu业务层接口
 */
public interface SpuService {

    /**
     * 逻辑删除
     *
     * @param id
     */
    void logicDelete(Long id);

    /**
     * 逻辑还原
     * @param id
     */
    void restore(Long id);

    /**
     * 商品批量下架
     *
     * @param ids
     * @return
     */
    int pullmany(Long[] ids);

    /**
     * 商品批量上架
     *
     * @param ids
     * @return
     */
    int putmany(Long[] ids);

    /**
     * 商品下架
     *
     * @param id
     */
    void pull(Long id);

    /**
     * 商品上架
     *
     * @param id
     */
    void put(Long id);

    /**
     * 商品审核
     *
     * @param id
     */
    void audit(Long id);

    /**
     * 根据spuId查询商品
     *
     * @param spuId
     * @return
     */
    Goods findGoodsById(Long spuId);

    /**
     * 商品增加、修改
     *
     * @param goods
     */
    void saveGoods(Goods goods);

    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     *
     * @param id
     * @return
     */
    Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();
}
