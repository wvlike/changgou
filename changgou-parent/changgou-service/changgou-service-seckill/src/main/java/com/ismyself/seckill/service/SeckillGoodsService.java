package com.ismyself.seckill.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * @Author:txw
 * @Description:SeckillGoods业务层接口
 */
public interface SeckillGoodsService {




    /**
     * 根据用户名、商品id、时间查询秒杀商品
     * @param id
     * @param time
     * @return
     */
    SeckillGoods getOne(Long id,String time);


    /**
     * 根据指定开始时间获取秒杀菜单
     * @param dateStr
     * @return
     */
    List<SeckillGoods> getDateList(String dateStr);


    /***
     * SeckillGoods多条件分页查询
     * @param seckillGoods
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size);

    /***
     * SeckillGoods分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillGoods> findPage(int page, int size);

    /***
     * SeckillGoods多条件搜索方法
     * @param seckillGoods
     * @return
     */
    List<SeckillGoods> findList(SeckillGoods seckillGoods);

    /***
     * 删除SeckillGoods
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillGoods数据
     * @param seckillGoods
     */
    void update(SeckillGoods seckillGoods);

    /***
     * 新增SeckillGoods
     * @param seckillGoods
     */
    void add(SeckillGoods seckillGoods);

    /**
     * 根据ID查询SeckillGoods
     * @param id
     * @return
     */
     SeckillGoods findById(Long id);

    /***
     * 查询所有SeckillGoods
     * @return
     */
    List<SeckillGoods> findAll();
}
